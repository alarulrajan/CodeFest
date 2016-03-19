package com.technoetic.xplanner.actions;

import java.util.Collection;
import java.util.Iterator;

import net.sf.xplanner.domain.UserStory;

/**
 * The Class DomainOrderer.
 */
public class DomainOrderer {
    
    /**
     * Builds the story id new order array.
     *
     * @param stories
     *            the stories
     * @return the int[][]
     */
    public static int[][] buildStoryIdNewOrderArray(final Collection stories) {
        final int[][] storyIdAndNewOrder = new int[stories.size()][2];
        int index = 0;
        for (final Iterator iterator = stories.iterator(); iterator.hasNext(); index++) {
            final UserStory userStory = (UserStory) iterator.next();
            storyIdAndNewOrder[index][0] = userStory.getId();
            storyIdAndNewOrder[index][1] = userStory.getOrderNo();
        }
        return storyIdAndNewOrder;
    }
}
