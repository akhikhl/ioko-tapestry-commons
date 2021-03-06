package uk.co.ioko.tapestry.mixins.mixins;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.ClientBehaviorSupport;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

@Import(stylesheet = "overlay.css", library = {"jquery.tools.min.js", "overlay.js"}, stack = "Jquery")
public class Overlay {
    @InjectContainer
    private ClientElement container;

    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String zone;

    @Parameter(allowNull = false)
    private boolean expose;

    @Parameter(allowNull = false, defaultPrefix = "literal")
    private String exposeColour;

    @Environmental
    private JavaScriptSupport renderSupport;

    @Environmental
    private ClientBehaviorSupport clientBehaviorSupport;

    private String overlayClientId;

    @SetupRender
    public void setup() {
        // allocate the ids we will need
        overlayClientId = renderSupport.allocateClientId("overlay");
    }

    @AfterRender
    void after(MarkupWriter writer) {
        writer.element("div", "class", "overlay", "id", overlayClientId);

        // render an inner div where we'll attach the zone for update
        writer.element("div", "id", zone);
        writer.end();
        writer.end();
        clientBehaviorSupport.addZone(zone, "show", "show");

        boolean form = (container instanceof Form);
        JSONObject spec = new JSONObject();
        spec.put("isForm", form ? "true" : "false");
        spec.put("overlayId", overlayClientId);
        spec.put("containerId", container.getClientId());
        spec.put("exposeColour", expose ? exposeColour
                : "none");
        renderSupport.addInitializerCall("overlayMixinLoad", spec);
    }

    private boolean defaultExpose() {
        return true;
    }

    private String defaultExposeColour() {
        return "#333333";
    }

}
