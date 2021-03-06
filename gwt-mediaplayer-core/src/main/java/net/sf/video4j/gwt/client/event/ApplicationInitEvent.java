/**
 * 
 */
package net.sf.video4j.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

/**
 * @author luc
 *
 */
public class ApplicationInitEvent extends GwtEvent<ApplicationInitEvent.ApplicationInitHandler> {
	
    protected ApplicationInitEvent() {
    }
        
    public static void fire(HasHandlers pSource) {
    	ApplicationInitEvent oEventInstance = new ApplicationInitEvent();
        pSource.fireEvent(oEventInstance);
    }

    //public static void fire(HasHandlers pSource, ApplicationInitEvent pEventInstance) {
    //    pSource.fireEvent(pEventInstance);
    //}

    public interface ApplicationInitHandlers extends HasHandlers {
        HandlerRegistration addApplicationReadyHandler(ApplicationInitHandler pHandler);
    }

    public interface ApplicationInitHandler extends EventHandler {
        public void onApplicationInitEvent(ApplicationInitEvent pEvent);
    }

    private static final Type<ApplicationInitHandler> TYPE = new Type<ApplicationInitHandler>();

    public static Type<ApplicationInitHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<ApplicationInitHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ApplicationInitHandler pHandler) {
        pHandler.onApplicationInitEvent(this);
    }
    
}

