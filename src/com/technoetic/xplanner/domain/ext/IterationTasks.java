package com.technoetic.xplanner.domain.ext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import net.sf.xplanner.domain.Iteration;
import net.sf.xplanner.domain.UserStory;

//DEBT: We may need to fold this into Iteration. See if there are other places that use this otherwise keep it this way
public class IterationTasks extends Iteration {

	Iteration iteration;

	public IterationTasks(final Iteration iteration) {
		this.iteration = iteration;
	}

	public Collection getIterationTasks() {
		final Collection iterationTasks = new HashSet();
		final Iterator storyIterator = this.iteration.getUserStories()
				.iterator();
		while (storyIterator.hasNext()) {
			final UserStory userStory = (UserStory) storyIterator.next();
			final Collection storyTasks = userStory.getTasks();
			iterationTasks.addAll(storyTasks);
		}
		return iterationTasks;
	}
}
