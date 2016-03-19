package com.technoetic.xplanner.tags;

import junit.framework.TestCase;
import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.Project;
import net.sf.xplanner.domain.UserStory;

/**
 * The Class TestStoryModel.
 */
public class TestStoryModel extends TestCase {
    
    /** The model. */
    StoryModel model;

    /** Test get name.
     *
     * @throws Exception
     *             the exception
     */
    public void testGetName() throws Exception {
        UserStory story = new UserStory();
        story.setName("story");
        Iteration iteration = new Iteration();
        iteration.setName("iteration");
        IterationModel model = new IterationModel(iteration){
            protected Project getProject() {
                Project project = new Project();
                project.setName("project");
                return project;
            }
        };
        this.model = new StoryModel(model,story );
        assertEquals("project :: iteration :: story",this.model.getName());
    }
}