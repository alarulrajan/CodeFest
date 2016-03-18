package com.sabre.security.jndi;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameNotFoundException;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.security.auth.Subject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.sabre.security.jndi.util.Base64;
import com.sabre.security.jndi.util.HexUtils;

public class JNDIAuthenticatorImpl implements JNDIAuthenticator {

	/**
	 * Debug level
	 */
	private int debug = 10;
	/**
	 * Logger.
	 */
	public static final Logger log = Logger
			.getLogger(JNDIAuthenticatorImpl.class);
	/**
	 * Should we search the entire subtree for matching users?
	 */
	private boolean userSubtree;
	/**
	 * Should we search the entire subtree for matching memberships?
	 */
	private boolean roleSubtree;
	/**
	 * Descriptive information about this Realm implementation.
	 */
	protected static final String info = "org.apache.catalina.realm.JNDIRealm/1.0";
	/**
	 * The JNDI context factory used to acquire our InitialContext. By default,
	 * assumes use of an LDAP server using the standard JNDI LDAP provider.
	 */
	protected String contextFactory = "com.sun.jndi.ldap.LdapCtxFactory";
	/**
	 * The type of authentication to use
	 */
	private String authentication;
	/**
	 * The connection username for the server we will contact.
	 */
	private String connectionUser;
	/**
	 * The connection password for the server we will contact.
	 */
	private String connectionPassword;
	/**
	 * The connection URL for the server we will contact.
	 */
	private String connectionURL;
	/**
	 * The protocol that will be used in the communication with the directory
	 * server.
	 */
	private String protocol;
	/**
	 * How should we handle referrals? Microsoft Active Directory can't handle
	 * the default case, so an application authenticating against AD must set
	 * referrals to "follow".
	 */
	private String referrals;
	/**
	 * The base element for user searches.
	 */
	private String userBase = "";
	/**
	 * The attribute name used to retrieve the user password.
	 */
	private String userPassword;
	/**
	 * The message format used to form the distinguished name of a user, with
	 * "{0}" marking the spot where the specified username goes.
	 */
	private String userPattern;
	/**
	 * The base element for role searches.
	 */
	private String roleBase = "";
	/**
	 * The name of an attribute in the user's entry containing roles for that
	 * user
	 */
	private String userRoleName;
	/**
	 * The name of the attribute containing roles held elsewhere
	 */
	private String roleName;
	/**
	 * The directory context linking us to our directory server.
	 */
	protected DirContext context;
	/**
	 * The MessageDigest
	 */
	private MessageDigest messageDigest;
	/**
	 * The MessageFormat object associated with the current
	 * <code>userSearch</code>.
	 */
	protected MessageFormat userSearchFormat;
	/**
	 * The MessageFormat object associated with the current
	 * <code>userPattern</code>.
	 */
	protected MessageFormat userPatternFormat;
	/**
	 * The MessageFormat object associated with the current
	 * <code>roleSearch</code>.
	 */
	protected MessageFormat roleFormat;

	@Override
	public void setDigest(final String algorithm)
			throws NoSuchAlgorithmException {
		this.messageDigest = MessageDigest.getInstance(algorithm);
	}

	public void setDebug(final String debug) {
		this.debug = Integer.parseInt(debug);
	}

	@Override
	public void setUserSubtree(final String userSubtree) {
		this.userSubtree = Boolean.getBoolean(userSubtree);
	}

	@Override
	public void setRoleSubtree(final String roleSubtree) {
		this.roleSubtree = Boolean.getBoolean(roleSubtree);
	}

	@Override
	public void setContextFactory(final String contextFactory) {
		this.contextFactory = contextFactory;
	}

	@Override
	public void setAuthentication(final String authentication) {
		this.authentication = authentication;
	}

	@Override
	public void setConnectionUser(final String connectionUser) {
		this.connectionUser = connectionUser;
	}

	@Override
	public void setConnectionPassword(final String connectionPassword) {
		this.connectionPassword = connectionPassword;
	}

	@Override
	public void setConnectionURL(final String connectionURL) {
		this.connectionURL = connectionURL;
	}

	@Override
	public void setProtocol(final String protocol) {
		this.protocol = protocol;
	}

	@Override
	public void setReferrals(final String referrals) {
		this.referrals = referrals;
	}

	@Override
	public void setUserBase(final String userBase) {
		this.userBase = userBase;
	}

	@Override
	public void setUserSearch(final String userSearch) {
		if (userSearch == null) {
			this.userSearchFormat = null;
		} else {
			this.userSearchFormat = new MessageFormat(userSearch);
		}
	}

	@Override
	public void setUserPassword(final String userPassword) {
		this.userPassword = userPassword;
	}

	@Override
	public void setUserPattern(final String userPattern) {
		this.userPattern = userPattern;
		if (userPattern == null) {
			this.userPatternFormat = null;
		} else {
			this.userPatternFormat = new MessageFormat(userPattern);
		}
	}

	@Override
	public void setRoleBase(final String roleBase) {
		this.roleBase = roleBase;
	}

	@Override
	public void setUserRoleName(final String userRoleName) {
		this.userRoleName = userRoleName;
	}

	@Override
	public void setRoleName(final String roleName) {
		this.roleName = roleName;
	}

	@Override
	public void setRoleSearch(final String roleSearch) {
		if (roleSearch == null) {
			this.roleFormat = null;
		} else {
			this.roleFormat = new MessageFormat(roleSearch);
		}
	}

	public void logMap(final Map options) {
		for (final Iterator iterator = options.keySet().iterator(); iterator
				.hasNext();) {
			final Object key = iterator.next();
			JNDIAuthenticatorImpl.log.debug("option: " + key + "="
					+ options.get(key));
		}
	}

	/**
	 * Return the Principal associated with the specified username and
	 * credentials, if there is one; otherwise return <code>null</code>.
	 * <p/>
	 * If there are any errors with the JDBC connection, executing the query or
	 * anything we return null (don't authenticate). This event is also logged,
	 * and the connection will be closed so that a subsequent request will
	 * automatically re-open it.
	 * 
	 * @param username
	 *            Username of the Principal to look up
	 * @param credentials
	 *            Password or other credentials to use in authenticating this
	 *            username
	 */
	@Override
	public synchronized Subject authenticate(final String username,
			final String credentials) throws AuthenticationException {

		this.openConnectionCheckCredentials(username, credentials);
		return this.getSubject(username, credentials);

	}

	private void openConnectionCheckCredentials(final String username,
			final String credentials) throws AuthenticationException {

		try {
			if (this.isAnonymousConnection()) {

				JNDIAuthenticatorImpl.log.debug("Anonymous connection");
				this.open();
				this.checkCredentials(username, credentials);

			} else if (this.isFixedUserConnection()) {

				JNDIAuthenticatorImpl.log.debug("Fixed user connection");
				this.open(this.connectionUser, this.connectionPassword);
				this.checkCredentials(username, credentials);

			} else {

				if (this.userPattern != null) {
					final String formattedUsername = MessageFormat.format(
							this.userPattern, new Object[] { username });
					JNDIAuthenticatorImpl.log.debug("Connection with "
							+ formattedUsername + " user");
					this.open(formattedUsername, credentials);
				}

			}
		} finally {
			this.closeContext();
		}

	}

	@Override
	public void setOptions(final Map options) {
		try {
			BeanUtils.copyProperties(this, options);
			if (this.debug >= 2) {
				this.logMap(options);
			}
		} catch (final Exception e) {
			Log.warn(e.getMessage());
		}

	}

	private Subject getSubject(final String username, final String credentials)
			throws AuthenticationException {
		if (username == null || username.equals("") || credentials == null
				|| credentials.equals("")) {
			throw new AuthenticationException(
					JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
		}
		final Subject subject = new Subject();
		final Principal principal = new Principal() {

			@Override
			public String getName() {
				return username;
			}
		};
		subject.getPrincipals().add(principal);
		return subject;
	}

	/**
	 * Return a User object containing information about the user with the
	 * specified username, if found in the directory; otherwise return
	 * <code>null</code>.
	 * <p/>
	 * If the <code>userPassword</code> configuration attribute is specified,
	 * the value of that attribute is retrieved from the user's directory entry.
	 * If the <code>userRoleName</code> configuration attribute is specified,
	 * all values of that attribute are retrieved from the directory entry.
	 * 
	 * @param username
	 *            Username to be looked up
	 * @throws javax.naming.NamingException
	 *             if a directory server error occurs
	 */
	protected User getUser(final String username) throws NamingException {

		User user = null;

		// Get attributes to retrieve from user entry
		final ArrayList list = new ArrayList();
		if (this.userPassword != null) {
			list.add(this.userPassword);
		}
		if (this.userRoleName != null) {
			list.add(this.userRoleName);
		}
		final String[] attrIds = new String[list.size()];
		list.toArray(attrIds);

		// Use pattern or search for user entry
		if (this.userPatternFormat != null) {
			this.log("Finding user by pattern.");
			user = this.getUserByPattern(username, attrIds);
		} else {
			this.log("Finding user by search. Subtree is " + this.userSubtree);
			user = this.getUserBySearch(username, attrIds);
		}

		return user;
	}

	/**
	 * Use the <code>UserPattern</code> configuration attribute to locate the
	 * directory entry for the user with the specified username and return a
	 * User object; otherwise return <code>null</code>.
	 * 
	 * @param username
	 *            The username
	 * @param attrIds
	 *            String[]containing names of attributes to retrieve.
	 * @throws javax.naming.NamingException
	 *             if a directory server error occurs
	 */
	protected User getUserByPattern(final String username,
			final String[] attrIds) throws NamingException {

		if (this.debug >= 2) {
			this.log("lookupUser(" + username + ")");
		}

		if (username == null || this.userPatternFormat == null) {
			return null;
		}

		// Form the dn from the user pattern
		final String dn = this.userPatternFormat
				.format(new String[] { username });
		if (this.debug >= 3) {
			this.log("  dn=" + dn);
		}

		// Return if no attributes to retrieve
		if (attrIds == null || attrIds.length == 0) {
			return new User(username, dn, null, null);
		}

		// Get required attributes from user entry
		Attributes attrs = null;
		try {
			attrs = this.context.getAttributes(dn, attrIds);
		} catch (final NameNotFoundException e) {
			Log.warn(e.getMessage());
			return null;
		}
		if (attrs == null) {
			return null;
		}

		// Retrieve value of userPassword
		String password = null;
		if (this.userPassword != null) {
			password = this.getAttributeValue(this.userPassword, attrs);
		}

		// Retrieve values of userRoleName attribute
		List roles = null;
		if (this.userRoleName != null) {
			roles = this.addAttributeValues(this.userRoleName, attrs, roles);
		}

		return new User(username, dn, password, roles);
	}

	/**
	 * Search the directory to return a User object containing information about
	 * the user with the specified username, if found in the directory;
	 * otherwise return <code>null</code>.
	 * 
	 * @param username
	 *            The username
	 * @param attrIds
	 *            String[]containing names of attributes to retrieve.
	 * @throws javax.naming.NamingException
	 *             if a directory server error occurs
	 */
	protected User getUserBySearch(final String username, String[] attrIds)
			throws NamingException {

		if (username == null || this.userSearchFormat == null) {
			if (username == null) {
				this.log("getUserBySearch impossible - username is null.");
			}
			if (this.userSearchFormat == null) {
				this.log("getUserBySearch impossible - userSearchFormat is null.");
			}
			return null;
		}

		// Form the search filter
		final String filter = this.userSearchFormat
				.format(new String[] { username });

		// Set up the search controls
		final SearchControls constraints = new SearchControls();

		if (this.userSubtree) {
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
		} else {
			constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		}

		// Specify the attributes to be retrieved
		if (attrIds == null) {
			attrIds = new String[0];
		}
		constraints.setReturningAttributes(attrIds);

		if (this.debug > 3) {
			this.log("  Searching for " + username);
			this.log("  base: " + this.userBase + "  filter: " + filter);
		}

		final NamingEnumeration results = this.context.search(this.userBase,
				filter, constraints);

		// Fail if no entries found
		if (results == null || !results.hasMore()) {
			if (this.debug > 2) {
				this.log("  username not found");
			}
			return null;
		}

		// Get result for the first entry found
		final SearchResult result = (SearchResult) results.next();

		// Check no further entries were found
		if (results.hasMore()) {
			this.log("username " + username + " has multiple entries");
			return null;
		}

		// Get the entry's distinguished name
		final NameParser parser = this.context.getNameParser("");
		final Name contextName = parser
				.parse(this.context.getNameInNamespace());
		final Name baseName = parser.parse(this.userBase);
		final Name entryName = parser.parse(result.getName());
		Name name = contextName.addAll(baseName);
		name = name.addAll(entryName);
		final String dn = name.toString();

		if (this.debug > 2) {
			this.log("  entry found for " + username + " with dn " + dn);
		}

		// Get the entry's attributes
		final Attributes attrs = result.getAttributes();
		if (attrs == null) {
			return null;
		}

		// Retrieve value of userPassword
		String password = null;
		if (this.userPassword != null) {
			password = this.getAttributeValue(this.userPassword, attrs);
		}

		// Retrieve values of userRoleName attribute
		List roles = null;
		if (this.userRoleName != null) {
			roles = this.addAttributeValues(this.userRoleName, attrs, roles);
		}

		return new User(username, dn, password, roles);
	}

	public boolean isAnonymousConnection() {
		return this.userPattern == null && this.connectionUser == null;
	}

	/**
	 * Check credentials by binding to the directory as the user
	 * 
	 * @param user
	 *            The User to be authenticated
	 * @param credentials
	 *            Authentication credentials
	 * @throws javax.naming.NamingException
	 *             if a directory server error occurs
	 */
	protected boolean bindAsUser(final User user, final String credentials)
			throws NamingException {

		if (credentials == null || user == null) {
			return false;
		}

		final String dn = user.dn;
		if (dn == null) {
			return false;
		}

		// Validate the credentials
		if (this.debug >= 3) {
			this.log("  validating credentials by binding as the user");
		}

		// Set up security environment to bind as the user
		this.context.addToEnvironment(Context.SECURITY_PRINCIPAL, dn);
		this.context
				.addToEnvironment(Context.SECURITY_CREDENTIALS, credentials);

		// Elicit an LDAP bind operation
		boolean validated = false;
		try {
			if (this.debug > 2) {
				this.log("  binding as " + dn);
			}
			this.context.getAttributes("", null);
			validated = true;
		} catch (final javax.naming.AuthenticationException e) {
			Log.warn(e.getMessage());
			if (this.debug > 2) {
				this.log("  bind attempt failed");
			}
		}

		// Restore the original security environment
		if (this.connectionUser != null) {
			this.context.addToEnvironment(Context.SECURITY_PRINCIPAL,
					this.connectionUser);
		} else {
			this.context.removeFromEnvironment(Context.SECURITY_PRINCIPAL);
		}

		if (this.connectionPassword != null) {
			this.context.addToEnvironment(Context.SECURITY_CREDENTIALS,
					this.connectionPassword);
		} else {
			this.context.removeFromEnvironment(Context.SECURITY_CREDENTIALS);
		}

		return validated;
	}

	/**
	 * Return a List of roles associated with the given User. Any roles present
	 * in the user's directory entry are supplemented by a directory search. If
	 * no roles are associated with this user, a zero-length List is returned.
	 * 
	 * @param user
	 *            The User to be checked
	 * @throws javax.naming.NamingException
	 *             if a directory server error occurs
	 */
	public List getRoles(final User user) throws NamingException {

		if (user == null) {
			return null;
		}

		final String dn = user.dn;
		final String username = user.username;

		if (dn == null || username == null) {
			return null;
		}

		if (this.debug >= 2) {
			this.log("  getRoles(" + dn + ")");
		}

		// Start with roles retrieved from the user entry
		List list = user.roles;
		if (list == null) {
			list = new ArrayList();
		}

		// Are we configured to do role searches?
		if (this.roleFormat == null || this.roleName == null) {
			return list;
		}

		// Set up parameters for an appropriate search
		final String filter = this.roleFormat.format(new String[] { dn,
				username });
		final SearchControls controls = new SearchControls();
		if (this.roleSubtree) {
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		} else {
			controls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		}
		controls.setReturningAttributes(new String[] { this.roleName });

		// Perform the configured search and process the results
		if (this.debug >= 3) {
			this.log("  Searching role base '" + this.roleBase
					+ "' for attribute '" + this.roleName + "'");
			this.log("  With filter expression '" + filter + "'");
		}
		final NamingEnumeration results = this.context.search(this.roleBase,
				filter, controls);
		if (results == null) {
			return list; // Should never happen, but just in case ...
		}
		while (results.hasMore()) {
			final SearchResult result = (SearchResult) results.next();
			final Attributes attrs = result.getAttributes();
			if (attrs == null) {
				continue;
			}
			list = this.addAttributeValues(this.roleName, attrs, list);
		}

		// Return the augmented list of roles
		if (this.debug >= 2) {
			this.log("  Returning " + list.size() + " roles");
			for (int i = 0; i < list.size(); i++) {
				this.log("  Found role " + list.get(i));
			}
		}

		return list;
	}

	/**
	 * Return a String representing the value of the specified attribute.
	 * 
	 * @param attrId
	 *            Attribute name
	 * @param attrs
	 *            Attributes containing the required value
	 * @throws javax.naming.NamingException
	 *             if a directory server error occurs
	 */
	private String getAttributeValue(final String attrId, final Attributes attrs)
			throws NamingException {

		if (this.debug >= 3) {
			this.log("  retrieving attribute " + attrId);
		}

		if (attrId == null || attrs == null) {
			return null;
		}

		final Attribute attr = attrs.get(attrId);
		if (attr == null) {
			return null;
		}

		final Object value = attr.get();
		if (value == null) {
			return null;
		}
		String valueString = null;
		if (value instanceof byte[]) {
			valueString = new String((byte[]) value);
		} else {
			valueString = value.toString();
		}

		return valueString;
	}

	/**
	 * Add values of a specified attribute to a list
	 * 
	 * @param attrId
	 *            Attribute name
	 * @param attrs
	 *            Attributes containing the new values
	 * @param values
	 *            ArrayList containing values found so far
	 * @throws javax.naming.NamingException
	 *             if a directory server error occurs
	 */
	private List addAttributeValues(final String attrId,
			final Attributes attrs, List values) throws NamingException {

		if (this.debug >= 3) {
			this.log("  retrieving values for attribute " + attrId);
		}
		if (attrId == null || attrs == null) {
			return null;
		}
		if (values == null) {
			values = new ArrayList();
		}
		final Attribute attr = attrs.get(attrId);
		if (attr == null) {
			return null;
		}
		final NamingEnumeration e = attr.getAll();
		while (e.hasMore()) {
			final String value = (String) e.next();
			values.add(value);
		}
		return values;
	}

	/**
	 * Create our directory context configuration.
	 * 
	 * @return java.util.Hashtable the configuration for the directory context.
	 */
	protected Hashtable getDirectoryContextEnvironment(final String username,
			final String credentials) {

		final Hashtable env = new Hashtable();

		this.setAttributeIfValueNotNull(env, Context.PROVIDER_URL,
				this.connectionURL);
		this.setAttributeIfValueNotNull(env, Context.INITIAL_CONTEXT_FACTORY,
				this.contextFactory);
		this.setAttributeIfValueNotNull(env, Context.SECURITY_PRINCIPAL,
				username);
		this.setAttributeIfValueNotNull(env, Context.SECURITY_CREDENTIALS,
				credentials);
		this.setAttributeIfValueNotNull(env, Context.SECURITY_AUTHENTICATION,
				this.authentication);
		this.setAttributeIfValueNotNull(env, Context.SECURITY_PROTOCOL,
				this.protocol);
		this.setAttributeIfValueNotNull(env, Context.REFERRAL, this.referrals);

		return env;
	}

	private void setAttributeIfValueNotNull(final Hashtable env,
			final String attribute, final String value) {
		if (value != null) {
			env.put(attribute, value);
		}
	}

	/**
	 * Check whether the given User can be authenticated with the given
	 * credentials. If the <code>userPassword</code> configuration attribute is
	 * specified, the credentials previously retrieved from the directory are
	 * compared explicitly with those presented by the user. Otherwise the
	 * presented credentials are checked by binding to the directory as the
	 * user.
	 * 
	 * @param userName
	 *            The UserName to be authenticated
	 * @param credentials
	 *            The credentials presented by the user
	 * @throws com.sabre.security.jndi.AuthenticationException
	 *             if a directory server error occurs if authentication fails
	 */
	public void checkCredentials(final String userName, final String credentials)
			throws com.sabre.security.jndi.AuthenticationException {

		boolean validated = false;
		try {
			final User user = this.getUser(userName);

			if (this.userPassword == null) {
				validated = this.bindAsUser(user, credentials);
				if (user != null) {
					this.log("bindAsUser(user, credentials) '" + user.username
							+ "'");
				} else {
					this.log("bindAsUser(user, credentials)");
				}
			} else {
				validated = this.compareCredentials(user, credentials);
				if (user != null) {
					this.log("compareCredentials(user, credentials) '"
							+ user.username + "'");
				} else {
					this.log("compareCredentials(user, credentials)");
				}
			}

			if (this.debug >= 2) {
				if (user != null) {
					this.log("jndiRealm.authenticate"
							+ (validated ? "Success" : "Failure" + " "
									+ user.username));
				} else {
					this.log("jndiRealm.authenticate"
							+ (validated ? "Success" : "Failure"));
				}
			}
		} catch (final CommunicationException ex) {
			this.log("jndiRealm.exception", ex);
			this.closeContext();
			throw new com.sabre.security.jndi.AuthenticationException(
					JNDIAuthenticator.MESSAGE_COMMUNICATION_ERROR_KEY);
		} catch (final NamingException ne) {
			throw new com.sabre.security.jndi.AuthenticationException(
					JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
		}
		if (!validated) {
			throw new com.sabre.security.jndi.AuthenticationException(
					JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
		}
	}

	/**
	 * Check whether the credentials presented by the user match those retrieved
	 * from the directory.
	 * 
	 * @param info
	 *            The User to be authenticated
	 * @param credentials
	 *            Authentication credentials
	 * @throws javax.naming.NamingException
	 *             if a directory server error occurs
	 */
	protected boolean compareCredentials(final User info,
			final String credentials) throws NamingException {

		if (info == null || credentials == null) {
			return false;
		}

		String password = info.password;
		if (password == null) {
			return false;
		}

		// Validate the credentials specified by the user
		if (this.debug >= 3) {
			this.log("  validating credentials");
		}

		boolean validated = false;
		if (this.hasMessageDigest()) {
			// Hex hashes should be compared case-insensitive
			if (Base64.isBase64(password.substring(5))) {
				password = HexUtils.convert(Base64.decode(password.substring(5)
						.getBytes()));
			}
			validated = this.digest(credentials).equalsIgnoreCase(password);
		} else {
			validated = this.digest(credentials).equals(password);
		}
		return validated;
	}

	protected String digest(final String credentials) {
		// If no MessageDigest instance is specified, return unchanged
		if (!this.hasMessageDigest()) {
			return credentials;
		}

		// Digest the user credentials and return as hexadecimal
		synchronized (this) {
			try {
				this.messageDigest.reset();
				this.messageDigest.update(credentials.getBytes());
				return HexUtils.convert(this.messageDigest.digest());
			} catch (final Exception e) {
				this.log("realmBase.digest", e);
				return credentials;
			}
		}
	}

	/**
	 * Close any open connection to the directory server for this Realm.
	 */
	public void closeContext() {

		// Do nothing if there is no opened connection
		if (this.context == null) {
			return;
		}

		// Close our opened connection
		try {
			if (this.debug >= 1) {
				this.log("Closing directory context");
			}
			this.context.close();
		} catch (final NamingException e) {
			this.log("jndiRealm.close", e);
		}
		// this.context = null;

	}

	/**
	 * Open (if necessary) and return a connection to the configured directory
	 * server for this Realm.
	 * 
	 * @throws com.sabre.security.jndi.AuthenticationException
	 *             if a directory server error occurs
	 */
	public void open() throws com.sabre.security.jndi.AuthenticationException {

		this.open(null, null);

	}

	public void open(final String username, final String credentials)
			throws com.sabre.security.jndi.AuthenticationException {

		try {

			// Ensure that we have a directory context available
			this.context = new InitialDirContext(
					this.getDirectoryContextEnvironment(username, credentials));

		} catch (final NamingException e) {

			try {

				// log the first exception.
				this.log("jndiRealm.exception", e);

				// Try connecting once more.
				this.context = new InitialDirContext(
						this.getDirectoryContextEnvironment(username,
								credentials));

			} catch (final CommunicationException ex) {
				this.log("jndiRealm.exception", ex);
				this.closeContext();
				throw new com.sabre.security.jndi.AuthenticationException(
						JNDIAuthenticator.MESSAGE_COMMUNICATION_ERROR_KEY);
			} catch (final NamingException ne) {
				Log.warn(e.getMessage());
				throw new com.sabre.security.jndi.AuthenticationException(
						JNDIAuthenticator.MESSAGE_AUTHENTICATION_FAILED_KEY);
			}
		}

	}

	public boolean isFixedUserConnection() {
		return this.connectionUser != null && this.connectionPassword != null;
	}

	private boolean hasMessageDigest() {
		return this.messageDigest != null;
	}

	private void log(final String s) {
		JNDIAuthenticatorImpl.log.info(s);
	}

	private void log(final String s, final Throwable ex) {
		JNDIAuthenticatorImpl.log.info(s, ex);
	}

}
