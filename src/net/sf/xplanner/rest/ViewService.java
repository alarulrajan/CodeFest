package net.sf.xplanner.rest;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.sf.xplanner.domain.view.ProjectView;
import net.sf.xplanner.domain.view.UserStoryView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class ViewService.
 */
@Path("/view")
@Component()
@Scope("request")
public class ViewService {
    
    /** The view manager. */
    private ViewManager viewManager;

    /**
     * Sets the view manager.
     *
     * @param viewManager
     *            the new view manager
     */
    @Autowired
    public void setViewManager(final ViewManager viewManager) {
        this.viewManager = viewManager;
    }

    /**
     * Gets the it.
     *
     * @return the it
     */
    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media
    // type "text/plain"
    @Produces("text/plain")
    public String getIt() {
        return "Hi there!";
    }

    /**
     * Gets the project.
     *
     * @param id
     *            the id
     * @return the project
     */
    @GET
    @Produces({ "application/xml", "application/json" })
    @Path("/project/{projectId}")
    public ProjectView getProject(@PathParam("projectId") final Integer id) {
        return this.viewManager.getProject(id);
    }

    /**
     * Gets the user stories.
     *
     * @param id
     *            the id
     * @return the user stories
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/iteration/{iterationId}/userstories")
    public UserStoryView[] getUserStories(
            @PathParam("iterationId") final Integer id) {
        final List<UserStoryView> userStories = this.viewManager
                .getUserStories(id);
        return userStories.toArray(new UserStoryView[userStories.size()]);
    }

}
