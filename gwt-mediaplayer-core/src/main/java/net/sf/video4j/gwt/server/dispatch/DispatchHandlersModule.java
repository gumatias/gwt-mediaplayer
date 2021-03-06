package net.sf.video4j.gwt.server.dispatch;

import java.util.logging.Logger;

import net.sf.video4j.gwt.shared.FetchAdAction;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

/**
 * @author gumatias
 */
public class DispatchHandlersModule extends HandlerModule {
    
    protected Logger mLogger = Logger.getLogger(this.getClass().getName());

    @Override
    protected void configureHandlers() {
        bindHandler(FetchAdAction.class, FetchAdHandler.class);
    }
    
}