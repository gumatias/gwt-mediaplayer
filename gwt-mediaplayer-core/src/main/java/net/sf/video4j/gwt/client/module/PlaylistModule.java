/**
 * 
 */
package net.sf.video4j.gwt.client.module;

import net.sf.video4j.gwt.client.controller.PlaylistController;

import com.google.gwt.inject.client.AbstractGinModule;

/**
 * @author luc
 *
 */
public class PlaylistModule extends AbstractGinModule {

	@Override
	protected void configure() {
		bind(PlaylistController.class);
	}

}
