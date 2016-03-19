package net.sf.xplanner.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * The Class UpdateService.
 */
@Path("/update")
@Component
@Scope("request")
public class UpdateService {
    
    /** The update manager. */
    private UpdateManager updateManager;

    /**
     * Sets the update manager.
     *
     * @param updateManager
     *            the new update manager
     */
    @Autowired
    public void setUpdateManager(final UpdateManager updateManager) {
        this.updateManager = updateManager;
    }

    /**
     * Post.
     *
     * @param id
     *            the id
     * @param status
     *            the status
     * @param originalEstimate
     *            the original estimate
     * @return the result
     */
    @POST
    @Path("/task/{taskId}/status")
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ "application/xml", "application/json" })
    public Result post(@PathParam("taskId") final int id,
            @FormParam("status") final String status,
            @FormParam("originalEstimate") final double originalEstimate) {
        return this.updateManager
                .updateTaskStatus(id, status, originalEstimate);
    }
}
