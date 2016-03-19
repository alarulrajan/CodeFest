package com.technoetic.mocks.hibernate;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.*;
import org.hibernate.classic.Session;
import org.hibernate.jdbc.Work;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;

/**
 * The Class MockSession.
 */
public class MockSession implements Session {
   
   /** The flush called. */
   public boolean flushCalled;
   
   /** The flush hibernate exception. */
   public HibernateException flushHibernateException;

   /* (non-Javadoc)
    * @see org.hibernate.Session#flush()
    */
   public void flush() throws HibernateException {
      flushCalled = true;
      if (flushHibernateException != null) {
         throw flushHibernateException;
      }
   }

   /** The connection called. */
   public boolean connectionCalled;
   
   /** The connection return. */
   public Connection connectionReturn;
   
   /** The connection hibernate exception. */
   public HibernateException connectionHibernateException;

   /* (non-Javadoc)
    * @see org.hibernate.Session#connection()
    */
   public Connection connection() throws HibernateException {
      connectionCalled = true;
      if (connectionHibernateException != null) {
         throw connectionHibernateException;
      }
      return connectionReturn;
   }

   /** The disconnect called. */
   public boolean disconnectCalled;
   
   /** The disconnect return. */
   public Connection disconnectReturn;
   
   /** The disconnect hibernate exception. */
   public HibernateException disconnectHibernateException;

   /* (non-Javadoc)
    * @see org.hibernate.Session#disconnect()
    */
   public Connection disconnect() throws HibernateException {
      disconnectCalled = true;
      if (disconnectHibernateException != null) {
         throw disconnectHibernateException;
      }
      return disconnectReturn;
   }

   /** The reconnect called. */
   public boolean reconnectCalled;
   
   /** The reconnect hibernate exception. */
   public HibernateException reconnectHibernateException;

   /* (non-Javadoc)
    * @see org.hibernate.Session#reconnect()
    */
   public void reconnect() throws HibernateException {
      reconnectCalled = true;
      if (reconnectHibernateException != null) {
         throw reconnectHibernateException;
      }
   }

   /** The reconnect2 called. */
   public boolean reconnect2Called;
   
   /** The reconnect2 hibernate exception. */
   public HibernateException reconnect2HibernateException;
   
   /** The reconnect2 connection. */
   public Connection reconnect2Connection;

   /* (non-Javadoc)
    * @see org.hibernate.Session#reconnect(java.sql.Connection)
    */
   public void reconnect(Connection connection) throws HibernateException {
      reconnect2Called = true;
      reconnect2Connection = connection;
      if (reconnect2HibernateException != null) {
         throw reconnect2HibernateException;
      }
   }

   /** The close called. */
   public boolean closeCalled;
   
   /** The close return. */
   public Connection closeReturn;
   
   /** The close hibernate exception. */
   public HibernateException closeHibernateException;

   /* (non-Javadoc)
    * @see org.hibernate.Session#close()
    */
   public Connection close() throws HibernateException {
      closeCalled = true;
      if (closeHibernateException != null) {
         throw closeHibernateException;
      }
      return closeReturn;
   }

   /** The is open called. */
   public boolean isOpenCalled;
   
   /** The is open return. */
   public Boolean isOpenReturn;

   /* (non-Javadoc)
    * @see org.hibernate.Session#isOpen()
    */
   public boolean isOpen() {
      isOpenCalled = true;
      return isOpenReturn.booleanValue();
   }

   /** The is connected called. */
   public boolean isConnectedCalled;
   
   /** The is connected return. */
   public Boolean isConnectedReturn;

   /* (non-Javadoc)
    * @see org.hibernate.Session#isConnected()
    */
   public boolean isConnected() {
      isConnectedCalled = true;
      return isConnectedReturn.booleanValue();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#isDirty()
    */
   public boolean isDirty() throws HibernateException {
      return false;
   }

   /** The get identifier called. */
   public boolean getIdentifierCalled;
   
   /** The get identifier return. */
   public Serializable getIdentifierReturn;
   
   /** The get identifier hibernate exception. */
   public HibernateException getIdentifierHibernateException;
   
   /** The get identifier object. */
   public Object getIdentifierObject;

   /* (non-Javadoc)
    * @see org.hibernate.Session#getIdentifier(java.lang.Object)
    */
   public Serializable getIdentifier(Object object) throws HibernateException {
      getIdentifierCalled = true;
      getIdentifierObject = object;
      if (getIdentifierHibernateException != null) {
         throw getIdentifierHibernateException;
      }
      return getIdentifierReturn;
   }

   /** The load called. */
   public boolean loadCalled;
   
   /** The load return. */
   public Object loadReturn;
   
   /** The load return map. */
   public HashMap loadReturnMap = new HashMap();
   
   /** The load hibernate exception. */
   public HibernateException loadHibernateException;
   
   /** The load the class. */
   public Class loadTheClass;
   
   /** The load id. */
   public Serializable loadId;

   /* (non-Javadoc)
    * @see org.hibernate.Session#load(java.lang.Class, java.io.Serializable)
    */
   public Object load(Class theClass, Serializable id) throws HibernateException {
      loadCalled = true;
      loadTheClass = theClass;
      loadId = id;
      if (loadHibernateException != null) {
         throw loadHibernateException;
      }
      if (loadReturn != null) {
         return loadReturn;
      } else {
         return getLoadReturn(theClass, id);
      }
   }

   /** Gets the load return.
     *
     * @param theClass
     *            the the class
     * @param id
     *            the id
     * @return the load return
     */
   private Object getLoadReturn(Class theClass, Serializable id) {
      Object o = loadReturnMap.get(theClass);
      if (o instanceof HashMap) {
         return ((HashMap) o).get(id);
      }
      return o;
   }

   /** Load add return by class by id.
     *
     * @param id
     *            the id
     * @param o
     *            the o
     */
   public void loadAddReturnByClassById(int id, Object o) {
      loadAddReturnByClassById(new Integer(id), o);
   }

   /** Load add return by class by id.
     *
     * @param id
     *            the id
     * @param o
     *            the o
     */
   private void loadAddReturnByClassById(Serializable id, Object o) {
      if (loadReturnMap == null) {
         loadReturn = new HashMap();
      }
      Object objectForClass = loadReturnMap.get(o.getClass());
      if (objectForClass == null) {
         objectForClass = new HashMap();
         loadReturnMap.put(o.getClass(), objectForClass);
      }
      if (!(objectForClass instanceof HashMap)) {
         throw new AssertionFailedError(
               "loadReturn already contains a map of single object per class. Cannot add multiple object of same class");
      }
      ((HashMap) objectForClass).put(id, o);

   }

   /** The load2 called. */
   public boolean load2Called;
   
   /** The load2 return. */
   public Object load2Return;
   
   /** The load2 hibernate exception. */
   public HibernateException load2HibernateException;
   
   /** The load2 the class. */
   public Class load2TheClass;
   
   /** The load2 id. */
   public Serializable load2Id;
   
   /** The load2 lock mode. */
   public LockMode load2LockMode;

   /* (non-Javadoc)
    * @see org.hibernate.Session#load(java.lang.Class, java.io.Serializable, org.hibernate.LockMode)
    */
   public Object load(Class theClass, Serializable id, LockMode lockMode) throws HibernateException {
      load2Called = true;
      load2TheClass = theClass;
      load2Id = id;
      load2LockMode = lockMode;
      if (load2HibernateException != null) {
         throw load2HibernateException;
      }
      return load2Return;
   }

   /** The load3 called. */
   public boolean load3Called;
   
   /** The load3 return. */
   public Object load3Return;
   
   /** The load3 hibernate exception. */
   public HibernateException load3HibernateException;
   
   /** The load3 id. */
   public Serializable load3Id;
   
   /** The load3 object. */
   public Object load3Object;

   /* (non-Javadoc)
    * @see org.hibernate.Session#load(java.lang.Object, java.io.Serializable)
    */
   public void load(Object object, Serializable id) throws HibernateException {
      load3Called = true;
      load3Object = object;
      load3Id = id;
      if (load3HibernateException != null) {
         throw load3HibernateException;
      }
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#get(java.lang.Class, java.io.Serializable)
    */
   public Object get(Class clazz, Serializable id) throws HibernateException {
      return load(clazz, id);
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#get(java.lang.Class, java.io.Serializable, org.hibernate.LockMode)
    */
   public Object get(Class clazz, Serializable id, LockMode lockMode) throws HibernateException {
      return load(clazz, id, lockMode);
   }

   /** The save called. */
   public boolean saveCalled;
   
   /** The save called count. */
   public int saveCalledCount;
   
   /** The save return. */
   public Serializable saveReturn;
   
   /** The save hibernate exception. */
   public HibernateException saveHibernateException;
   
   /** The save object. */
   public Object saveObject;
   
   /** The save objects. */
   public ArrayList saveObjects = new ArrayList();
   
   /** The save ids. */
   public Object[] saveIds;
   
   /** The save id property. */
   public String saveIdProperty;

   /* (non-Javadoc)
    * @see org.hibernate.Session#save(java.lang.Object)
    */
   public Serializable save(Object object) throws HibernateException {
      saveCalled = true;
      saveCalledCount++;
      saveObject = object;
      saveObjects.add(object);
      if (saveHibernateException != null) {
         throw saveHibernateException;
      }
      if (saveIdProperty != null) {
         try {
            saveReturn = (Integer) saveIds[saveCalledCount - 1];
            PropertyUtils.setProperty(object, saveIdProperty, saveReturn);
         } catch (Exception e) {
            throw new HibernateException(e);
         }
      }
      return saveReturn;
   }

   /** The save2 called. */
   public boolean save2Called;
   
   /** The save2 return. */
   public Serializable save2Return;
   
   /** The save2 hibernate exception. */
   public HibernateException save2HibernateException;
   
   /** The save2 object. */
   public Object save2Object;
   
   /** The save2 id. */
   public Serializable save2Id;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#save(java.lang.Object, java.io.Serializable)
    */
   public void save(Object object, Serializable id) throws HibernateException {
      save2Called = true;
      save2Object = object;
      save2Id = id;
      if (save2HibernateException != null) {
         throw save2HibernateException;
      }
   }

   /** The save or update called. */
   public boolean saveOrUpdateCalled;
   
   /** The save or update hibernate exception. */
   public HibernateException saveOrUpdateHibernateException;
   
   /** The save or update object. */
   public Object saveOrUpdateObject;

   /* (non-Javadoc)
    * @see org.hibernate.Session#saveOrUpdate(java.lang.Object)
    */
   public void saveOrUpdate(Object object) throws HibernateException {
      saveOrUpdateCalled = true;
      saveOrUpdateObject = object;
      if (saveOrUpdateHibernateException != null) {
         throw saveOrUpdateHibernateException;
      }
   }

   /** The update called. */
   public boolean updateCalled;
   
   /** The update hibernate exception. */
   public HibernateException updateHibernateException;
   
   /** The update object. */
   public Object updateObject;

   /* (non-Javadoc)
    * @see org.hibernate.Session#update(java.lang.Object)
    */
   public void update(Object object) throws HibernateException {
      updateCalled = true;
      updateObject = object;
      if (updateHibernateException != null) {
         throw updateHibernateException;
      }
   }


   /** The update2 called. */
   public boolean update2Called;
   
   /** The update2 hibernate exception. */
   public HibernateException update2HibernateException;
   
   /** The update2 object. */
   public Object update2Object;
   
   /** The update2 id. */
   public Serializable update2Id;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#update(java.lang.Object, java.io.Serializable)
    */
   public void update(Object object, Serializable id) throws HibernateException {
      update2Called = true;
      update2Object = object;
      update2Id = id;
      if (update2HibernateException != null) {
         throw update2HibernateException;
      }
   }

   /** The delete called. */
   public boolean deleteCalled;
   
   /** The delete hibernate exception. */
   public HibernateException deleteHibernateException;
   
   /** The delete object. */
   public Object deleteObject;

   /* (non-Javadoc)
    * @see org.hibernate.Session#delete(java.lang.Object)
    */
   public void delete(Object object) throws HibernateException {
      deleteCalled = true;
      deleteObject = object;
      if (deleteHibernateException != null) {
         throw deleteHibernateException;
      }
   }

   /** The find called. */
   public boolean findCalled;
   
   /** The find return. */
   public List findReturn;
   
   /** The find hibernate exception. */
   public HibernateException findHibernateException;
   
   /** The find query. */
   public String findQuery;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#find(java.lang.String)
    */
   public List find(String query) throws HibernateException {
      findCalled = true;
      findQuery = query;
      if (findHibernateException != null) {
         throw findHibernateException;
      }
      return findReturn;
   }

   /** The find2 called. */
   public boolean find2Called;
   
   /** The find2 return. */
   public List find2Return;
   
   /** The find2 called count. */
   public int find2CalledCount;
   
   /** The find2 returns. */
   public List find2Returns; // list of lists
   
   /** The find2 return empty lists. */
   public boolean find2ReturnEmptyLists;
   
   /** The find2 hibernate exception. */
   public HibernateException find2HibernateException;
   
   /** The find2 query. */
   public String find2Query;
   
   /** The find2 value. */
   public Object find2Value;
   
   /** The find2 type. */
   public Type find2Type;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#find(java.lang.String, java.lang.Object, org.hibernate.type.Type)
    */
   public List find(String query, Object value, Type type) throws HibernateException {
      find2Called = true;
      find2CalledCount++;
      find2Query = query;
      find2Value = value;
      find2Type = type;
      if (find2HibernateException != null) {
         throw find2HibernateException;
      }
      if (find2ReturnEmptyLists && (find2Returns == null || find2CalledCount > find2Returns.size())) {
         return Collections.EMPTY_LIST;
      }
      if (find2Returns != null) {
         return (List) find2Returns.get(find2CalledCount - 1);
      } else {
         return find2Return;
      }
   }

   /** The find3 called. */
   public boolean find3Called;
   
   /** The find3 return. */
   public List find3Return;
   
   /** The find3 called count. */
   public int find3CalledCount;
   
   /** The find3 returns. */
   public List find3Returns; // list of lists
   
   /** The find3 return empty lists. */
   public boolean find3ReturnEmptyLists;
   
   /** The find3 hibernate exception. */
   public HibernateException find3HibernateException;
   
   /** The find3 query. */
   public String find3Query;
   
   /** The find3 values. */
   public Object[] find3Values;
   
   /** The find3 types. */
   public Type[] find3Types;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#find(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
    */
   public List find(String query, Object[] values, Type[] types) throws HibernateException {
      find3Called = true;
      find3CalledCount++;
      find3Query = query;
      find3Values = values;
      find3Types = types;
      if (find3HibernateException != null) {
         throw findHibernateException;
      }
      if (find3ReturnEmptyLists && (find3Returns == null || find3CalledCount >= find3Returns.size())) {
         return Collections.EMPTY_LIST;
      }
      if (find3Returns != null) {
         return (List) find3Returns.get(find3CalledCount - 1);
      } else {
         return find3Return;
      }
   }

   /** The iterate called. */
   public boolean iterateCalled;
   
   /** The iterate return. */
   public Iterator iterateReturn;
   
   /** The iterate hibernate exception. */
   public HibernateException iterateHibernateException;
   
   /** The iterate query. */
   public String iterateQuery;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#iterate(java.lang.String)
    */
   public Iterator iterate(String query) throws HibernateException {
      iterateCalled = true;
      iterateQuery = query;
      if (iterateHibernateException != null) {
         throw iterateHibernateException;
      }
      return iterateReturn;
   }

   /** The iterate2 called. */
   public boolean iterate2Called;
   
   /** The iterate2 return. */
   public Iterator iterate2Return;
   
   /** The iterate2 hibernate exception. */
   public HibernateException iterate2HibernateException;
   
   /** The iterate2 value. */
   public Object iterate2Value;
   
   /** The iterate2 type. */
   public Type iterate2Type;
   
   /** The iterate2 query. */
   public String iterate2Query;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#iterate(java.lang.String, java.lang.Object, org.hibernate.type.Type)
    */
   public Iterator iterate(String query, Object value, Type type) throws HibernateException {
      iterate2Called = true;
      iterate2Query = query;
      iterate2Value = value;
      iterate2Type = type;
      if (iterateHibernateException != null) {
         throw iterate2HibernateException;
      }
      return iterate2Return;
   }


   /** The iterate3 called. */
   public boolean iterate3Called;
   
   /** The iterate3 return. */
   public Iterator iterate3Return;
   
   /** The iterate3 hibernate exception. */
   public HibernateException iterate3HibernateException;
   
   /** The iterate3 values. */
   public Object[] iterate3Values;
   
   /** The iterate3 types. */
   public Type[] iterate3Types;
   
   /** The iterate3 query. */
   public String iterate3Query;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#iterate(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
    */
   public Iterator iterate(String query, Object[] values, Type[] types) throws HibernateException {
      iterate3Called = true;
      iterate3Query = query;
      iterate3Values = values;
      iterate3Types = types;
      if (iterate3HibernateException != null) {
         throw iterate3HibernateException;
      }
      return iterate3Return;
   }

   /** The filter called. */
   public boolean filterCalled;
   
   /** The filter return. */
   public Collection filterReturn;
   
   /** The filter hibernate exception. */
   public HibernateException filterHibernateException;
   
   /** The filter collection. */
   public Object filterCollection;
   
   /** The filter filter. */
   public String filterFilter;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#filter(java.lang.Object, java.lang.String)
    */
   public Collection filter(Object collection, String filter) throws HibernateException {
      filterCalled = true;
      filterCollection = collection;
      filterFilter = filter;
      if (filterHibernateException != null) {
         throw filterHibernateException;
      }
      return filterReturn;
   }


   /** The filter2 called. */
   public boolean filter2Called;
   
   /** The filter2 return. */
   public Collection filter2Return;
   
   /** The filter2 hibernate exception. */
   public HibernateException filter2HibernateException;
   
   /** The filter2 collection. */
   public Object filter2Collection;
   
   /** The filter2 filter. */
   public String filter2Filter;
   
   /** The filter2 value. */
   public Object filter2Value;
   
   /** The filter2 type. */
   public Type filter2Type;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#filter(java.lang.Object, java.lang.String, java.lang.Object, org.hibernate.type.Type)
    */
   public Collection filter(Object collection, String filter, Object value, Type type)
         throws HibernateException {
      filter2Called = true;
      filter2Collection = collection;
      filter2Filter = filter;
      filter2Value = value;
      filter2Type = type;
      if (filter2HibernateException != null) {
         throw filterHibernateException;
      }
      return filter2Return;
   }

   /** The filter3 called. */
   public boolean filter3Called;
   
   /** The filter3 return. */
   public Collection filter3Return;
   
   /** The filter3 hibernate exception. */
   public HibernateException filter3HibernateException;
   
   /** The filter3 collection. */
   public Object filter3Collection;
   
   /** The filter3 filter. */
   public String filter3Filter;
   
   /** The filter3 values. */
   public Object[] filter3Values;
   
   /** The filter3 types. */
   public Type[] filter3Types;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#filter(java.lang.Object, java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
    */
   public Collection filter(Object collection, String filter, Object[] values, Type[] types)
         throws HibernateException {
      filter3Called = true;
      filter3Collection = collection;
      filter3Filter = filter;
      filter3Values = values;
      filter3Types = types;
      if (filterHibernateException != null) {
         throw filter3HibernateException;
      }
      return filter3Return;
   }

   /** The delete2 called. */
   public boolean delete2Called;
   
   /** The delete2 return. */
   public Integer delete2Return;
   
   /** The delete2 hibernate exception. */
   public HibernateException delete2HibernateException;
   
   /** The delete2 query. */
   public String delete2Query;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#delete(java.lang.String)
    */
   public int delete(String query) throws HibernateException {
      delete2Called = true;
      delete2Query = query;
      if (delete2HibernateException != null) {
         throw delete2HibernateException;
      }
      return delete2Return.intValue();
   }

   /** The delete3 called. */
   public boolean delete3Called;
   
   /** The delete3 return. */
   public Integer delete3Return;
   
   /** The delete3 hibernate exception. */
   public HibernateException delete3HibernateException;
   
   /** The delete3 query. */
   public String delete3Query;
   
   /** The delete3 value. */
   public Object delete3Value;
   
   /** The delete3 type. */
   public Type delete3Type;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#delete(java.lang.String, java.lang.Object, org.hibernate.type.Type)
    */
   public int delete(String query, Object value, Type type) throws HibernateException {
      delete3Called = true;
      delete3Query = query;
      delete3Value = value;
      delete3Type = type;
      if (delete3HibernateException != null) {
         throw delete3HibernateException;
      }
      return delete3Return.intValue();
   }


   /** The delete4 called count. */
   public int delete4CalledCount;
   
   /** The delete4 called. */
   public boolean delete4Called;
   
   /** The delete4 return. */
   public Integer delete4Return;
   
   /** The delete4 hibernate exception. */
   public HibernateException delete4HibernateException;
   
   /** The delete4 query. */
   public String delete4Query;
   
   /** The delete4 values. */
   public Object[] delete4Values;
   
   /** The delete4 types. */
   public Type[] delete4Types;

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#delete(java.lang.String, java.lang.Object[], org.hibernate.type.Type[])
    */
   public int delete(String query, Object[] values, Type[] types) throws HibernateException {
      delete4CalledCount++;
      delete4Called = true;
      delete4Query = query;
      delete4Values = values;
      delete4Types = types;
      if (delete4HibernateException != null) {
         throw delete4HibernateException;
      }
      return delete4Return.intValue();
   }

   /** The lock called. */
   public boolean lockCalled;
   
   /** The lock hibernate exception. */
   public HibernateException lockHibernateException;
   
   /** The lock object. */
   public Object lockObject;
   
   /** The lock lock mode. */
   public LockMode lockLockMode;

   /* (non-Javadoc)
    * @see org.hibernate.Session#lock(java.lang.Object, org.hibernate.LockMode)
    */
   public void lock(Object object, LockMode lockMode) throws HibernateException {
      lockCalled = true;
      lockObject = object;
      lockLockMode = lockMode;
      if (lockHibernateException != null) {
         throw lockHibernateException;
      }
   }

   /** The get current lock mode called. */
   public boolean getCurrentLockModeCalled;
   
   /** The get current lock mode return. */
   public LockMode getCurrentLockModeReturn;
   
   /** The get current lock mode hibernate exception. */
   public HibernateException getCurrentLockModeHibernateException;
   
   /** The get current lock mode object. */
   public Object getCurrentLockModeObject;

   /* (non-Javadoc)
    * @see org.hibernate.Session#getCurrentLockMode(java.lang.Object)
    */
   public LockMode getCurrentLockMode(Object object) throws HibernateException {
      getCurrentLockModeCalled = true;
      getCurrentLockModeObject = object;
      if (getCurrentLockModeHibernateException != null) {
         throw getCurrentLockModeHibernateException;
      }
      return getCurrentLockModeReturn;
   }

   /** The begin transaction called. */
   public boolean beginTransactionCalled;
   
   /** The begin transaction return. */
   public Transaction beginTransactionReturn;
   
   /** The begin transaction hibernate exception. */
   public HibernateException beginTransactionHibernateException;

   /* (non-Javadoc)
    * @see org.hibernate.Session#beginTransaction()
    */
   public Transaction beginTransaction() throws HibernateException {
      beginTransactionCalled = true;
      if (beginTransactionHibernateException != null) {
         throw beginTransactionHibernateException;
      }
      return beginTransactionReturn;
   }

   /** The suspend flushes called. */
   public boolean suspendFlushesCalled;

   /** Suspend flushes.
     */
   public void suspendFlushes() {
      suspendFlushesCalled = true;
   }

   /** The resume flushes called. */
   public boolean resumeFlushesCalled;

   /** Resume flushes.
     */
   public void resumeFlushes() {
      resumeFlushesCalled = true;
   }

   /** The create query called. */
   public boolean createQueryCalled;
   
   /** The create query return. */
   public Query createQueryReturn;
   
   /** The create query hibernate exception. */
   public HibernateException createQueryHibernateException;
   
   /** The create query query string. */
   public String createQueryQueryString;

   /* (non-Javadoc)
    * @see org.hibernate.Session#createQuery(java.lang.String)
    */
   public Query createQuery(String queryString) throws HibernateException {
      createQueryCalled = true;
      createQueryQueryString = queryString;
      if (createQueryHibernateException != null) {
         throw createQueryHibernateException;
      }
      return createQueryReturn;
   }

   /** The create filter called. */
   public boolean createFilterCalled;
   
   /** The create filter return. */
   public Query createFilterReturn;
   
   /** The create filter hibernate exception. */
   public HibernateException createFilterHibernateException;
   
   /** The create filter collection. */
   public Object createFilterCollection;
   
   /** The create filter query string. */
   public String createFilterQueryString;

   /* (non-Javadoc)
    * @see org.hibernate.Session#createFilter(java.lang.Object, java.lang.String)
    */
   public Query createFilter(Object collection, String queryString) throws HibernateException {
      createFilterCalled = true;
      createFilterCollection = collection;
      createFilterQueryString = queryString;
      if (createFilterHibernateException != null) {
         throw createFilterHibernateException;
      }
      return createFilterReturn;
   }

   /** The get named query called. */
   public boolean getNamedQueryCalled;
   
   /** The get named query return. */
   public Query getNamedQueryReturn;
   
   /** The get named query return map. */
   public HashMap getNamedQueryReturnMap = new HashMap();
   
   /** The get named query hibernate exception. */
   public HibernateException getNamedQueryHibernateException;
   
   /** The get named query query name. */
   public String getNamedQueryQueryName;

   /* (non-Javadoc)
    * @see org.hibernate.Session#getNamedQuery(java.lang.String)
    */
   public Query getNamedQuery(String queryName) throws HibernateException {
      getNamedQueryCalled = true;
      getNamedQueryQueryName = queryName;
      if (getNamedQueryHibernateException != null) {
         throw getNamedQueryHibernateException;
      }
      if (getNamedQueryReturn != null) return getNamedQueryReturn;

      return (Query) getNamedQueryReturnMap.get(queryName);
   }

   /** The find identifiers called. */
   public boolean findIdentifiersCalled;
   
   /** The find identifiers return. */
   public List findIdentifiersReturn;
   
   /** The find identifiers hibernate exception. */
   public HibernateException findIdentifiersHibernateException;
   
   /** The find identifiers query. */
   public String findIdentifiersQuery;

   /** Find identifiers.
     *
     * @param query
     *            the query
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     */
   public List findIdentifiers(String query) throws HibernateException {
      findIdentifiersCalled = true;
      findIdentifiersQuery = query;
      if (findIdentifiersHibernateException != null) {
         throw findIdentifiersHibernateException;
      }
      return findIdentifiersReturn;
   }

   /** The find identifiers2 called. */
   public boolean findIdentifiers2Called;
   
   /** The find identifiers2 return. */
   public List findIdentifiers2Return;
   
   /** The find identifiers2 hibernate exception. */
   public HibernateException findIdentifiers2HibernateException;
   
   /** The find identifiers2 query. */
   public String findIdentifiers2Query;
   
   /** The find identifiers2 value. */
   public Object findIdentifiers2Value;
   
   /** The find identifiers2 type. */
   public Type findIdentifiers2Type;

   /** Find identifiers.
     *
     * @param query
     *            the query
     * @param value
     *            the value
     * @param type
     *            the type
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     */
   public List findIdentifiers(String query, Object value, Type type) throws HibernateException {
      findIdentifiers2Called = true;
      findIdentifiers2Query = query;
      findIdentifiers2Value = value;
      findIdentifiers2Type = type;
      if (findIdentifiers2HibernateException != null) {
         throw findIdentifiers2HibernateException;
      }
      return findIdentifiers2Return;
   }

   /** The find identifiers3 called. */
   public boolean findIdentifiers3Called;
   
   /** The find identifiers3 return. */
   public List findIdentifiers3Return;
   
   /** The find identifiers3 hibernate exception. */
   public HibernateException findIdentifiers3HibernateException;
   
   /** The find identifiers3 query. */
   public String findIdentifiers3Query;
   
   /** The find identifiers3 values. */
   public Object[] findIdentifiers3Values;
   
   /** The find identifiers3 types. */
   public Type[] findIdentifiers3Types;

   /** Find identifiers.
     *
     * @param query
     *            the query
     * @param values
     *            the values
     * @param types
     *            the types
     * @return the list
     * @throws HibernateException
     *             the hibernate exception
     */
   public List findIdentifiers(String query, Object[] values, Type[] types) throws HibernateException {
      findIdentifiers3Called = true;
      findIdentifiers3Query = query;
      findIdentifiers3Values = values;
      findIdentifiers3Types = types;
      if (findIdentifiers3HibernateException != null) {
         throw findIdentifiers3HibernateException;
      }
      return findIdentifiers3Return;
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#setFlushMode(org.hibernate.FlushMode)
    */
   public void setFlushMode(FlushMode flushMode) {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#getFlushMode()
    */
   public FlushMode getFlushMode() {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#getSessionFactory()
    */
   public SessionFactory getSessionFactory() {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#cancelQuery()
    */
   public void cancelQuery() throws HibernateException {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#contains(java.lang.Object)
    */
   public boolean contains(Object object) {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#evict(java.lang.Object)
    */
   public void evict(Object object) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#replicate(java.lang.Object, org.hibernate.ReplicationMode)
    */
   public void replicate(Object object, ReplicationMode replicationMode) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#saveOrUpdateCopy(java.lang.Object)
    */
   public Object saveOrUpdateCopy(Object object) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#saveOrUpdateCopy(java.lang.Object, java.io.Serializable)
    */
   public Object saveOrUpdateCopy(Object object, Serializable id) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#refresh(java.lang.Object)
    */
   public void refresh(Object object) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#refresh(java.lang.Object, org.hibernate.LockMode)
    */
   public void refresh(Object object, LockMode lockMode) throws HibernateException {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#createCriteria(java.lang.Class)
    */
   public Criteria createCriteria(Class persistentClass) {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#createSQLQuery(java.lang.String, java.lang.String, java.lang.Class)
    */
   public Query createSQLQuery(String sql, String returnAlias, Class returnClass) {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.classic.Session#createSQLQuery(java.lang.String, java.lang.String[], java.lang.Class[])
    */
   public Query createSQLQuery(String sql, String[] returnAliases, Class[] returnClasses) {
      throw new UnsupportedOperationException();
   }

   /* (non-Javadoc)
    * @see org.hibernate.Session#clear()
    */
   public void clear() {
      throw new UnsupportedOperationException();
   }

/* (non-Javadoc)
 * @see org.hibernate.classic.Session#save(java.lang.String, java.lang.Object, java.io.Serializable)
 */
@Override
public void save(String s, Object obj, Serializable serializable)
        throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.classic.Session#saveOrUpdateCopy(java.lang.String, java.lang.Object)
 */
@Override
public Object saveOrUpdateCopy(String s, Object obj) throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.classic.Session#saveOrUpdateCopy(java.lang.String, java.lang.Object, java.io.Serializable)
 */
@Override
public Object saveOrUpdateCopy(String s, Object obj, Serializable serializable)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.classic.Session#update(java.lang.String, java.lang.Object, java.io.Serializable)
 */
@Override
public void update(String s, Object obj, Serializable serializable)
        throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#createCriteria(java.lang.String)
 */
@Override
public Criteria createCriteria(String s) {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#createCriteria(java.lang.Class, java.lang.String)
 */
@Override
public Criteria createCriteria(Class class1, String s) {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#createCriteria(java.lang.String, java.lang.String)
 */
@Override
public Criteria createCriteria(String s, String s1) {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#createSQLQuery(java.lang.String)
 */
@Override
public SQLQuery createSQLQuery(String s) throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#delete(java.lang.String, java.lang.Object)
 */
@Override
public void delete(String s, Object obj) throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#disableFilter(java.lang.String)
 */
@Override
public void disableFilter(String s) {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#enableFilter(java.lang.String)
 */
@Override
public Filter enableFilter(String s) {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#get(java.lang.String, java.io.Serializable)
 */
@Override
public Object get(String s, Serializable serializable)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#get(java.lang.String, java.io.Serializable, org.hibernate.LockMode)
 */
@Override
public Object get(String s, Serializable serializable, LockMode lockmode)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getCacheMode()
 */
@Override
public CacheMode getCacheMode() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getEnabledFilter(java.lang.String)
 */
@Override
public Filter getEnabledFilter(String s) {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getEntityMode()
 */
@Override
public EntityMode getEntityMode() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getEntityName(java.lang.Object)
 */
@Override
public String getEntityName(Object obj) throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getSession(org.hibernate.EntityMode)
 */
@Override
public org.hibernate.Session getSession(EntityMode entitymode) {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getStatistics()
 */
@Override
public SessionStatistics getStatistics() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getTransaction()
 */
@Override
public Transaction getTransaction() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#load(java.lang.String, java.io.Serializable)
 */
@Override
public Object load(String s, Serializable serializable)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#load(java.lang.String, java.io.Serializable, org.hibernate.LockMode)
 */
@Override
public Object load(String s, Serializable serializable, LockMode lockmode)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#lock(java.lang.String, java.lang.Object, org.hibernate.LockMode)
 */
@Override
public void lock(String s, Object obj, LockMode lockmode)
        throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#merge(java.lang.Object)
 */
@Override
public Object merge(Object obj) throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#merge(java.lang.String, java.lang.Object)
 */
@Override
public Object merge(String s, Object obj) throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#persist(java.lang.Object)
 */
@Override
public void persist(Object obj) throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#persist(java.lang.String, java.lang.Object)
 */
@Override
public void persist(String s, Object obj) throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#replicate(java.lang.String, java.lang.Object, org.hibernate.ReplicationMode)
 */
@Override
public void replicate(String s, Object obj, ReplicationMode replicationmode)
        throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#save(java.lang.String, java.lang.Object)
 */
@Override
public Serializable save(String s, Object obj) throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#saveOrUpdate(java.lang.String, java.lang.Object)
 */
@Override
public void saveOrUpdate(String s, Object obj) throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#setCacheMode(org.hibernate.CacheMode)
 */
@Override
public void setCacheMode(CacheMode cachemode) {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#setReadOnly(java.lang.Object, boolean)
 */
@Override
public void setReadOnly(Object obj, boolean flag) {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#update(java.lang.String, java.lang.Object)
 */
@Override
public void update(String s, Object obj) throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#doWork(org.hibernate.jdbc.Work)
 */
@Override
public void doWork(Work work) throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#isDefaultReadOnly()
 */
@Override
public boolean isDefaultReadOnly() {
    // ChangeSoon 
    return false;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#setDefaultReadOnly(boolean)
 */
@Override
public void setDefaultReadOnly(boolean readOnly) {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#load(java.lang.Class, java.io.Serializable, org.hibernate.LockOptions)
 */
@Override
public Object load(Class theClass, Serializable id, LockOptions lockOptions)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#load(java.lang.String, java.io.Serializable, org.hibernate.LockOptions)
 */
@Override
public Object load(String entityName, Serializable id, LockOptions lockOptions)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#buildLockRequest(org.hibernate.LockOptions)
 */
@Override
public LockRequest buildLockRequest(LockOptions lockOptions) {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#refresh(java.lang.Object, org.hibernate.LockOptions)
 */
@Override
public void refresh(Object object, LockOptions lockOptions)
        throws HibernateException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#get(java.lang.Class, java.io.Serializable, org.hibernate.LockOptions)
 */
@Override
public Object get(Class clazz, Serializable id, LockOptions lockOptions)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#get(java.lang.String, java.io.Serializable, org.hibernate.LockOptions)
 */
@Override
public Object get(String entityName, Serializable id, LockOptions lockOptions)
        throws HibernateException {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#isReadOnly(java.lang.Object)
 */
@Override
public boolean isReadOnly(Object entityOrProxy) {
    // ChangeSoon 
    return false;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#isFetchProfileEnabled(java.lang.String)
 */
@Override
public boolean isFetchProfileEnabled(String name)
        throws UnknownProfileException {
    // ChangeSoon 
    return false;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#enableFetchProfile(java.lang.String)
 */
@Override
public void enableFetchProfile(String name) throws UnknownProfileException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#disableFetchProfile(java.lang.String)
 */
@Override
public void disableFetchProfile(String name) throws UnknownProfileException {
    // ChangeSoon 
    
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getTypeHelper()
 */
@Override
public TypeHelper getTypeHelper() {
    // ChangeSoon 
    return null;
}

/* (non-Javadoc)
 * @see org.hibernate.Session#getLobHelper()
 */
@Override
public LobHelper getLobHelper() {
    // ChangeSoon 
    return null;
}

}
