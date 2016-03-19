
/*
 * Copyright (c) Mateusz Prokopowicz. All Rights Reserved.
 */

package com.technoetic.xplanner.charts;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import net.sf.xplanner.dao.DataSampleDao;
import net.sf.xplanner.domain.DataSample;
import net.sf.xplanner.domain.Iteration;

import org.hibernate.HibernateException;
import org.springframework.orm.hibernate3.HibernateOperations;

import com.technoetic.xplanner.AbstractUnitTestCase;
import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * The Class TestDataSamplerImpl.
 */
public class TestDataSamplerImpl extends AbstractUnitTestCase {
   
   /** The data sampler. */
   DataSamplerImpl dataSampler;
   
   /** The iteration. */
   Iteration iteration;
   
   /** The time generator. */
   TimeGenerator timeGenerator;
   
   /** The mock hibernate operations. */
   HibernateOperations mockHibernateOperations;
   
   /** The active iteration. */
   Iteration activeIteration;
   
   /** The today midnight. */
   private Date todayMidnight;
   
   /** The tomorrow midnight. */
   private Date tomorrowMidnight;
   
   /** The properties. */
   private Properties properties;
   
   /** The mock time generator. */
   private TimeGenerator mockTimeGenerator;
   
   /** The estimated hours data samples. */
   private final List estimatedHoursDataSamples = new ArrayList();
   
   /** The actual hours data samples. */
   private final List actualHoursDataSamples = new ArrayList();
   
   /** The remaining hours data sample. */
   private final List remainingHoursDataSample = new ArrayList();
   
   /** The data sample dao. */
   private DataSampleDao dataSampleDao;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#tearDown()
    */
   @Override
public void tearDown() throws Exception {
      super.tearDown();
   }

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.AbstractUnitTestCase#setUp()
    */
   @Override
protected void setUp() throws Exception {
      super.setUp();
      properties = new Properties();
      setAutomaticallyExtendIterationEndDate(false);
      iteration = new Iteration();
      iteration.setId(99);
      timeGenerator = new TimeGenerator();
      mockHibernateOperations = createLocalMock(HibernateOperations.class);
      mockTimeGenerator = createLocalMock(TimeGenerator.class);
      dataSampler = new DataSamplerImpl();
      dataSampler.setProperties(properties);
      dataSampler.setTimeGenerator(timeGenerator);
      dataSampler.setHibernateOperations(mockHibernateOperations);
      dataSampleDao = createLocalMock(DataSampleDao.class);
      dataSampler.setDataSampleDao(dataSampleDao);
      todayMidnight = TimeGenerator.getMidnight(new Date());
      tomorrowMidnight = TimeGenerator.shiftDate(todayMidnight,
                                                 Calendar.DAY_OF_MONTH,
                                                 1);
      DataSample dataSampleToDel1 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "remainingHours",
                                        1.0);
      DataSample dataSampleToDel2 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "actualHours",
                                        1.0);
      DataSample dataSampleToDel3 = new DataSample(tomorrowMidnight,
                                        iteration.getId(),
                                        "estimatedHours",
                                        1.0);

      estimatedHoursDataSamples.add(dataSampleToDel3);
      actualHoursDataSamples.add(dataSampleToDel2);
      remainingHoursDataSample.add(dataSampleToDel1);

      activeIteration = new Iteration();
      activeIteration.setId(99);
      activeIteration.setIterationStatus(IterationStatus.ACTIVE);
   }

   /** Test generate opening datasample.
     *
     * @throws HibernateException
     *             the hibernate exception
     */
   public void testGenerateOpeningDatasample() throws HibernateException {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(todayMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateOpeningDataSamples(iteration);
      verify();
   }

   /** Test generate datasample.
     */
   public void testGenerateDatasample() {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateDataSamples(iteration);
      verify();
   }

   /** Test update datasample.
     */
   public void testUpdateDatasample() {
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, estimatedHoursDataSamples,
                                                    actualHoursDataSamples,
                                                    remainingHoursDataSample);
      updateDataSamples();
      replay();
      dataSampler.generateDataSamples(iteration);
      verify();
   }


   /** Test generate closing datasample on iteration end date.
     */
   public void testGenerateClosingDatasampleOnIterationEndDate() {
      iteration.setEndDate(TimeGenerator.shiftDate(timeGenerator.getCurrentTime(),
                                                 Calendar.MINUTE,
                                                 10));

      checkIfDataSamplesHaveNotBeenAlreadyGenerated(tomorrowMidnight, Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST,
                                                    Collections.EMPTY_LIST);
      saveDataSamples();
      replay();
      dataSampler.generateClosingDataSamples(iteration);
      verify();
   }

   /** Test generate closing datasample after iteration end date.
     */
   public void testGenerateClosingDatasampleAfterIterationEndDate() {
      iteration.setEndDate(TimeGenerator.shiftDate(timeGenerator.getCurrentTime(),
                                                 Calendar.MINUTE,
                                                 -10));
      checkIfDataSamplesHaveNotBeenAlreadyGenerated(todayMidnight, estimatedHoursDataSamples,
                                                    actualHoursDataSamples,
                                                    remainingHoursDataSample);
      updateDataSamples();
      replay();
      dataSampler.generateClosingDataSamples(iteration);
      verify();


   }

   /** Test extend iteration end date if needed_ turned off.
     *
     * @throws Exception
     *             the exception
     */
   public void testExtendIterationEndDateIfNeeded_TurnedOff() throws Exception {
      iteration.setEndDate(todayMidnight);

      setAutomaticallyExtendIterationEndDate(false);
      assertEquals(todayMidnight, iteration.getEndDate());
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(todayMidnight, iteration.getEndDate());
   }

   /** Test extend iteration end date if needed_ turned on iteration is
     * active.
     *
     * @throws Exception
     *             the exception
     */
   public void testExtendIterationEndDateIfNeeded_TurnedOnIterationIsActive() throws Exception {
      iteration.setEndDate(todayMidnight);
      iteration.setIterationStatus(IterationStatus.ACTIVE);
      setAutomaticallyExtendIterationEndDate(true);
      expect(mockHibernateOperations.save(iteration)).andReturn(null);
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(tomorrowMidnight, iteration.getEndDate());
   }

   /** Test extend iteration end date if needed_ turned on iteration is
     * inactive.
     *
     * @throws Exception
     *             the exception
     */
   public void testExtendIterationEndDateIfNeeded_TurnedOnIterationIsInactive() throws Exception {
      iteration.setEndDate(todayMidnight);
      setAutomaticallyExtendIterationEndDate(true);
      assertEquals(todayMidnight, iteration.getEndDate());
      replay();
      dataSampler.extendIterationEndDateIfNeeded(iteration, tomorrowMidnight);
      verify();
      assertEquals(todayMidnight, iteration.getEndDate());
   }

   /** Check if data samples have not been already generated.
     *
     * @param samplingDate
     *            the sampling date
     * @param estimatedHoursDataSamples
     *            the estimated hours data samples
     * @param actualHoursDataSamples
     *            the actual hours data samples
     * @param remainingHoursDataSample
     *            the remaining hours data sample
     */
   private void checkIfDataSamplesHaveNotBeenAlreadyGenerated(Date samplingDate, List estimatedHoursDataSamples,
                                                              List actualHoursDataSamples,
                                                              List remainingHoursDataSample) {
       expect(dataSampleDao.getDataSamples(samplingDate, iteration, "estimatedHours")).andReturn(estimatedHoursDataSamples).times(0, 1);
       expect(dataSampleDao.getDataSamples(samplingDate, iteration, "actualHours")).andReturn(actualHoursDataSamples).times(0, 1);
       expect(dataSampleDao.getDataSamples(samplingDate, iteration, "remainingHours")).andReturn(remainingHoursDataSample).times(0, 1);
   }

   /** Save data samples.
     */
   private void saveDataSamples() {
      expect(dataSampleDao.save((DataSample) anyObject())).andReturn(1).times(0,3);
   }

   /** Update data samples.
     */
   private void updateDataSamples() {
       expect(dataSampleDao.save((DataSample) anyObject())).andReturn(1).times(3);
   }

   /** Sets the automatically extend iteration end date.
     *
     * @param automaticallyExtend
     *            the new automatically extend iteration end date
     */
   private void setAutomaticallyExtendIterationEndDate(boolean automaticallyExtend) {
      properties.setProperty(DataSamplerImpl.AUTOMATICALLY_EXTEND_END_DATE_PROP, Boolean.toString(automaticallyExtend));
   }
}