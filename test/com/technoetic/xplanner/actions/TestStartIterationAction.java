package com.technoetic.xplanner.actions;

import static org.easymock.EasyMock.expect;

import java.util.Date;

import net.sf.xplanner.domain.History;
import net.sf.xplanner.domain.Iteration;

import com.technoetic.xplanner.domain.IterationStatus;
import com.technoetic.xplanner.util.TimeGenerator;

/**
 * The Class TestStartIterationAction.
 */
public class TestStartIterationAction extends AbstractIterationStatusTestCase {
   
   /** The mock time generator. */
   TimeGenerator mockTimeGenerator;
   
   /** The sampling date. */
   private Date samplingDate;

   /* (non-Javadoc)
    * @see com.technoetic.xplanner.actions.AbstractIterationStatusTestCase#setUp()
    */
   public void setUp() throws Exception {
      action = new StartIterationAction();
      super.setUp();
      mockTimeGenerator = createLocalMock(TimeGenerator.class);
      ((StartIterationAction) action).setTimeGenerator(mockTimeGenerator);
      samplingDate = new Date();
      event.setWhenHappened(samplingDate);
   }

   /** Test execute with1 started iteration asks for confirmation.
     *
     * @throws Exception
     *             the exception
     */
   public void testExecuteWith1StartedIterationAsksForConfirmation() throws Exception {
      Iteration startedIteration = new Iteration();
      startedIteration.start();
      project.getIterations().add(startedIteration);
      editorForm.setIterationStartConfirmed(false);
      iteration.setIterationStatus(IterationStatus.INACTIVE);
      mockDataSampler.generateOpeningDataSamples(iteration);

      replay();

      support.executeAction(action);
      assertEquals("iteration status", IterationStatus.INACTIVE_KEY, iteration.getStatusKey());
      assertEquals("task estimates", 4.0d, story.getEstimatedOriginalHours(), 0.0);

   }

   /** Test execute with0 started iteration starts iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testExecuteWith0StartedIterationStartsIteration() throws Exception
   {
      editorForm.setIterationStartConfirmed(false);
      iteration.setIterationStatus(IterationStatus.INACTIVE);
      mockDataSampler.generateOpeningDataSamples(iteration);
      event.setAction(History.ITERATION_STARTED);
      expect(mockSession.save(event)).andReturn(null);
      expect(mockTimeGenerator.getCurrentTime()).andReturn(samplingDate);
      replay();

      support.executeAction(action);
      assertEquals("iteration status", IterationStatus.ACTIVE_KEY, iteration.getStatusKey());
      assertEquals("task estimates", 4.0d, story.getEstimatedOriginalHours(), 0.0);

   }

   /** Test execute after confirmation starts iteration.
     *
     * @throws Exception
     *             the exception
     */
   public void testExecuteAfterConfirmationStartsIteration() throws Exception
   {

      editorForm.setIterationStartConfirmed(true);
      iteration.setIterationStatus(IterationStatus.INACTIVE);
      mockDataSampler.generateOpeningDataSamples(iteration);

      event.setAction(History.ITERATION_STARTED);
      expect(mockTimeGenerator.getCurrentTime()).andReturn(samplingDate);
      expect(mockSession.save(event)).andReturn(null);

      replay();

      support.executeAction(action);
      assertEquals("iteration status", IterationStatus.ACTIVE_KEY, iteration.getStatusKey());
      assertEquals("task estimates", 4.0d, story.getEstimatedOriginalHours(), 0.0);
   }

}

