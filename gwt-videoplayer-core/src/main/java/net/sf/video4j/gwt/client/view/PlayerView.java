package net.sf.video4j.gwt.client.view;


import net.sf.video4j.gwt.client.handler.PlayerUiHandlers;
import net.sf.video4j.gwt.client.model.PlayerParameters;
import net.sf.video4j.gwt.client.presenter.PlayerPresenter;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

import fr.hd3d.html5.video.client.VideoSource;
import fr.hd3d.html5.video.client.VideoWidget;

/**
 * @author gumatias
 */
public class PlayerView extends ViewWithUiHandlers<PlayerUiHandlers> implements PlayerPresenter.PView {
    
    public interface Binder extends UiBinder<HTMLPanel, PlayerView> {
    }

    @UiField(provided = true)
    VideoWidget mVideoWidget;
	
    @Inject
    public PlayerView(Binder pBinder, VideoWidget pVideoWidget) {
        mVideoWidget = pVideoWidget;
        initWidget(pBinder.createAndBindUi(this));
    }

    @Override
    public void startPlayer(PlayerParameters pParams) {
        mVideoWidget.setControls(pParams.hasControls());
        mVideoWidget.setAutoPlay(pParams.isAutoPlay());
        mVideoWidget.addSource(new VideoSource(pParams.getFileSource(), pParams.getVideoType()));
        mVideoWidget.setPixelSize(pParams.getWidthInPixels(), pParams.getHeightInPixels());
    }
    
}