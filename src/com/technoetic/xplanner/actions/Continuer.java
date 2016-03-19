/*
 * Created by IntelliJ IDEA.
 * User: Jacques
 * Date: May 28, 2005
 * Time: 8:52:29 PM
 */
package com.technoetic.xplanner.actions;

import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;

import javassist.util.proxy.ProxyObject;
import net.sf.xplanner.domain.DomainObject;
import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Note;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.technoetic.xplanner.db.hibernate.ThreadSession;
import com.technoetic.xplanner.domain.Describable;
import com.technoetic.xplanner.domain.DomainMetaDataRepository;
import com.technoetic.xplanner.domain.DomainObjectWikiLinkFormatter;
import com.technoetic.xplanner.domain.NoteAttachable;
import com.technoetic.xplanner.history.HistorySupport;
import com.technoetic.xplanner.util.LogUtil;

/**
 * The Class Continuer.
 */
public abstract class Continuer {
    
    /** The Constant LOG. */
    protected static final Logger LOG = LogUtil.getLogger();

    /** The Constant CONTINUE_ACTION. */
    public static final String CONTINUE_ACTION = "Continue";
    
    /** The Constant MOVE_ACTION. */
    public static final String MOVE_ACTION = "Move";
    
    /** The to text. */
    protected MessageFormat toText;
    
    /** The from text. */
    protected MessageFormat fromText;
    
    /** The from parent text. */
    protected MessageFormat fromParentText;
    
    /** The to parent text. */
    protected MessageFormat toParentText;

    /** The link formatter. */
    protected DomainObjectWikiLinkFormatter linkFormatter;
    
    /** The current user id. */
    protected int currentUserId;
    
    /** The hibernate session. */
    private Session hibernateSession;
    
    /** The when. */
    protected Date when = new Date();
    
    /** The Constant CONTINUED_FROM_DESCRIPTION_KEY. */
    public static final String CONTINUED_FROM_DESCRIPTION_KEY = "continue.description.from";
    
    /** The Constant CONTINUED_AS_DESCRIPTION_KEY. */
    public static final String CONTINUED_AS_DESCRIPTION_KEY = "continue.description.to";
    
    /** The Constant CONTINUE_DESCRIPTION_TO_PARENT_KEY. */
    public static final String CONTINUE_DESCRIPTION_TO_PARENT_KEY = "continue.description.from_parent";
    
    /** The Constant CONTINUE_DESCRIPTION_FROM_PARENT_KEY. */
    public static final String CONTINUE_DESCRIPTION_FROM_PARENT_KEY = "continue.description.to_parent";
    
    /** The meta data repository. */
    protected DomainMetaDataRepository metaDataRepository;

    /** The history support. */
    private HistorySupport historySupport;

    /**
     * Instantiates a new continuer.
     */
    protected Continuer() {
    }

    /**
     * Inits the.
     *
     * @param session
     *            the session
     * @param messageResources
     *            the message resources
     * @param currentUserId
     *            the current user id
     */
    public final void init(final Session session,
            final MessageResources messageResources, final int currentUserId) {
        this.setHibernateSession(session);
        this.setLinkFormatter(new DomainObjectWikiLinkFormatter());
        this.setMessageResources(messageResources);
        this.setCurrentUserId(currentUserId);
    }

    /**
     * Sets the message resources.
     *
     * @param messageResources
     *            the new message resources
     */
    public void setMessageResources(final MessageResources messageResources) {
        this.toText = new MessageFormat(
                messageResources
                        .getMessage(Continuer.CONTINUED_FROM_DESCRIPTION_KEY));
        this.fromText = new MessageFormat(
                messageResources
                        .getMessage(Continuer.CONTINUED_AS_DESCRIPTION_KEY));
        this.fromParentText = new MessageFormat(
                messageResources
                        .getMessage(Continuer.CONTINUE_DESCRIPTION_FROM_PARENT_KEY));
        this.toParentText = new MessageFormat(
                messageResources
                        .getMessage(Continuer.CONTINUE_DESCRIPTION_TO_PARENT_KEY));
    }

    /**
     * Continue object.
     *
     * @param fromObject
     *            the from object
     * @param fromParent
     *            the from parent
     * @param toParent
     *            the to parent
     * @return the domain object
     * @throws HibernateException
     *             the hibernate exception
     */
    public DomainObject continueObject(final DomainObject fromObject,
            final DomainObject fromParent, final DomainObject toParent)
            throws HibernateException {

        Continuer.LOG.debug("hibernateSession used="
                + this.getHibernateSession());
        final DomainObject toObject = this.createContinuingObject(fromObject,
                toParent);

        this.doContinueObject(fromObject, toParent, toObject);

        // FIXME The original iteration should not have container events of
        // created action when continue a story in target targetIteration

        this.updateDescriptionAndHistory(fromObject, fromParent, toObject,
                toParent);

        this.continueNotes((NoteAttachable) fromObject,
                (NoteAttachable) toObject);

        this.getHibernateSession().update(fromObject);
        this.getHibernateSession().update(toParent);
        this.getHibernateSession().update(fromParent);

        return toObject;

    }

    /**
     * Sets the meta data repository.
     *
     * @param metaDataRepository
     *            the new meta data repository
     */
    public void setMetaDataRepository(
            final DomainMetaDataRepository metaDataRepository) {
        this.metaDataRepository = metaDataRepository;
    }

    /**
     * Creates the continuing object.
     *
     * @param fromObject
     *            the from object
     * @param toParent
     *            the to parent
     * @return the domain object
     * @throws HibernateException
     *             the hibernate exception
     */
    private DomainObject createContinuingObject(final DomainObject fromObject,
            final DomainObject toParent) throws HibernateException {
        final DomainObject toObject = this.cloneObject(fromObject);
        this.getHibernateSession().save(toObject);
        this.metaDataRepository.setParent(toObject, toParent);
        return toObject;
    }

    /**
     * Update description and history.
     *
     * @param fromObject
     *            the from object
     * @param fromParent
     *            the from parent
     * @param toObject
     *            the to object
     * @param toParent
     *            the to parent
     */
    private void updateDescriptionAndHistory(final DomainObject fromObject,
            final DomainObject fromParent, final DomainObject toObject,
            final DomainObject toParent) {
        final Object[] parentDescriptionParams = new Object[] {
                this.linkFormatter.format(fromObject),
                this.linkFormatter.format(fromParent),
                this.linkFormatter.format(toObject),
                this.linkFormatter.format(toParent) };
        this.updateDescription((Describable) toObject, this.fromText,
                parentDescriptionParams);
        this.updateDescription((Describable) fromObject, this.toText,
                parentDescriptionParams);

        this.addHistoryEvent(History.CONTINUED, fromObject,
                this.toText.format(parentDescriptionParams));
        this.addHistoryEvent(History.CONTINUED, toObject,
                this.fromText.format(parentDescriptionParams));
        this.addHistoryEvent(History.CONTINUED, fromParent,
                this.toParentText.format(parentDescriptionParams));
        this.addHistoryEvent(History.CONTINUED, toParent,
                this.fromParentText.format(parentDescriptionParams));
    }

    /**
     * Clone object.
     *
     * @param fromObject
     *            the from object
     * @return the domain object
     */
    private DomainObject cloneObject(final DomainObject fromObject) {
        try {
            final DomainObject toObject = this.createInstance(fromObject);
            BeanUtils.copyProperties(fromObject, toObject,
                    new String[] { "tasks" });
            toObject.setId(0);
            return toObject;

        } catch (final Exception e) {
            Continuer.LOG.error(e);
        }
        return null;
    }

    /**
     * Creates the instance.
     *
     * @param fromObject
     *            the from object
     * @return the domain object
     * @throws InstantiationException
     *             the instantiation exception
     * @throws IllegalAccessException
     *             the illegal access exception
     */
    protected DomainObject createInstance(final DomainObject fromObject)
            throws InstantiationException, IllegalAccessException {

        if (ProxyObject.class.isAssignableFrom(fromObject.getClass())) {
            return (DomainObject) fromObject.getClass().getSuperclass()
                    .newInstance();
        }
        return fromObject.getClass().newInstance();
    }

    /**
     * Do continue object.
     *
     * @param fromObject
     *            the from object
     * @param toParent
     *            the to parent
     * @param toObject
     *            the to object
     * @throws HibernateException
     *             the hibernate exception
     */
    protected abstract void doContinueObject(DomainObject fromObject,
            DomainObject toParent, DomainObject toObject)
            throws HibernateException;

    /**
     * Adds the history event.
     *
     * @param type
     *            the type
     * @param target
     *            the target
     * @param description
     *            the description
     */
    protected void addHistoryEvent(final String type,
            final DomainObject target, final String description) {
        this.historySupport.saveEvent(target, type, description,
                this.currentUserId, this.when);
    }

    /**
     * Update description.
     *
     * @param describable
     *            the describable
     * @param direction
     *            the direction
     * @param params
     *            the params
     */
    private void updateDescription(final Describable describable,
            final MessageFormat direction, final Object[] params) {
        describable.setDescription(direction.format(params) + "\n\n"
                + describable.getDescription());
    }

    /**
     * Continue notes.
     *
     * @param fromObject
     *            the from object
     * @param toObject
     *            the to object
     * @throws HibernateException
     *             the hibernate exception
     */
    public void continueNotes(final NoteAttachable fromObject,
            final NoteAttachable toObject) throws HibernateException {
        final Query query = this.getHibernateSession().getNamedQuery(
                Note.class.getName() + Note.ATTACHED_NOTES_QUERY);
        query.setString("attachedToId", String.valueOf(fromObject.getId()));
        final Iterator iterator = query.iterate();
        while (iterator.hasNext()) {
            final Note note = (Note) iterator.next();
            final Note newNote = (Note) this.cloneObject(note);
            newNote.setAttachedToId(toObject.getId());
            newNote.setId(0);
            this.getHibernateSession().save(newNote);
        }
    }

    /**
     * Gets the hibernate session.
     *
     * @return the hibernate session
     */
    public Session getHibernateSession() {
        if (this.hibernateSession != ThreadSession.get()) {
            Continuer.LOG.error("hibernateSession mismatch "
                    + this.hibernateSession + " and " + ThreadSession.get());
        }
        return this.hibernateSession;
    }

    /**
     * Sets the hibernate session.
     *
     * @param hibernateSession
     *            the new hibernate session
     */
    public void setHibernateSession(final Session hibernateSession) {
        this.hibernateSession = hibernateSession;
    }

    /**
     * Sets the current user id.
     *
     * @param currentUserId
     *            the new current user id
     */
    public void setCurrentUserId(final int currentUserId) {
        this.currentUserId = currentUserId;
    }

    /**
     * Sets the link formatter.
     *
     * @param linkFormatter
     *            the new link formatter
     */
    public void setLinkFormatter(
            final DomainObjectWikiLinkFormatter linkFormatter) {
        this.linkFormatter = linkFormatter;
    }

    /**
     * Sets the history support.
     *
     * @param historySupport
     *            the new history support
     */
    @Required
    @Autowired
    public void setHistorySupport(final HistorySupport historySupport) {
        this.historySupport = historySupport;
    }

}