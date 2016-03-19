package net.sf.xplanner.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * The Class CommonObjectHandler.
 */
@Controller
public class CommonObjectHandler {

    /**
     * List.
     *
     * @param objectType
     *            the object type
     * @return the string
     */
    @RequestMapping("/{objectType}/list")
    public String list(@PathVariable final String objectType) {
        System.out.println(objectType);

        return objectType;
    }

    /**
     * Edits the.
     *
     * @param objectType
     *            the object type
     * @param objectId
     *            the object id
     * @return the string
     */
    @RequestMapping("/{objectType}/edit/{objectId}")
    public String edit(@PathVariable final String objectType,
            @PathVariable final Integer objectId) {
        System.out.println(objectType);
        return objectType;
    }
}
