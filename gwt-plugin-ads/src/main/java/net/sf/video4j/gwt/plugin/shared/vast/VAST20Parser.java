/**
 * 
 */
package net.sf.video4j.gwt.plugin.shared.vast;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

/**
 * @author luc
 * 
 */
public class VAST20Parser {

	private static final String COLON = ":";
	private static final String CREATIVE_VIEW = "creativeView";
	private static final String EVENT = "event";
	private static final String TRACKING = "tracking";
	private static final String CUSTOM_CLICK = "CustomClick";
	private static final String CLICK_TRACKING = "ClickTracking";
	private static final String CLICK_THROUGH = "ClickThrough";
	private static final String PROGRESSIVE = "progressive";
	private static final String DELIVERY = "delivery";
	private static final String MAINTAIN_ASPECT_RATIO = "maintainAspectRatio";
	private static final String SCALABLE = "scalable";
	private static final String TYPE = "type";
	private static final String HEIGHT = "height";
	private static final String WIDTH = "width";
	private static final String EXPANDED_HEIGHT = "expandedHeight";
	private static final String EXPANDED_WIDTH = "expandedWidth";
	private static final String BITRATE = "bitrate";
	private static final String API_FRAMEWORK = "apiFramework";
	private static final String MEDIA_FILES = "MediaFiles";
	private static final String VIDEO_CLICKS = "VideoClicks";
	private static final String AD_PARAMETERS = "AdParameters";
	private static final String TRACKING_EVENTS = "TrackingEvents";
	private static final String DURATION = "Duration";
	private static final String SEQUENCE = "Sequence";
	private static final String AD_ID = "AdID";
	private static final String NON_LINEAR_ADS = "NonLinearAds";
	private static final String COMPANION_ADS = "CompanionAds";
	private static final String LINEAR = "Linear";
	private static final String NON_LINEAR = "NonLinear";
	private static final String CREATIVES = "Creatives";
	private static final String IMPRESSION = "Impression";
	private static final String ERROR = "Error";
	private static final String SURVEY = "Survey";
	private static final String DESCRIPTION = "Description";
	private static final String AD_TITLE = "AdTitle";
	private static final String VERSION = "version";
	private static final String AD_SYSTEM = "AdSystem";
	private static final String WRAPPER = "Wrapper";
	private static final String IN_LINE = "InLine";
	private static final String ID = "id";
	private static final String AD = "ad";
	private static final String MIN_SUGGESTED_DURATION = "minSuggestedDuration";
	private static final String NON_LINEAR_CLICK_THROUGH = "NonLinearClickThrough";
	private static final String STATIC_RESOURCE = "StaticResource";
	private static final String IFRAME_RESOURCE = "IFrameResource";
	private static final String HTML_RESOURCE = "HTMLResource";
	private static final String CREATIVE_TYPE = "creativeType";
	private static final String ALT_TEXT = "AltText";
	private static final String COMPANION_CLICK_THROUGH = "CompanionClickThrough";
	private static final String VAST_AD_TAG_URI = "VASTAdTagURI";
	private static final String EXTENSIONS = "Extensions";
	
	private Logger mLogger = Logger.getLogger(this.getClass().getName());

	public VAST parse(Node pRoot) {
		mLogger.log(Level.FINEST, "Parsing VAST 2.0");
		VAST oVAST = new VAST();
		NodeList oChildren = pRoot.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (n.getNodeName().equalsIgnoreCase(AD)) {
				Ad oAd = parseAd(n);
				oVAST.getAds().add(oAd);
			} else {
				mLogger.log(Level.WARNING, "Found node \"" + n.getNodeName() + "\" (type=" + n.getNodeType() + ") instead of <ad>.");
				//mLogger.log(Level.SEVERE, "Invalid VAST 2.0 document. Found: \"" + n.getNodeName() + "\", expecting \"" + AD + "\".");
				//throw new InvalidDocumentException(n.getNodeName(), AD);
			}
		}
		mLogger.log(Level.FINEST, "Done parsing VAST 2.0 " + oVAST);
		return oVAST;
	}

	private Ad parseAd(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing Ad...");
		Ad oResult = null;
		String oId = getAttribute(pNode, ID, null);
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength() && oResult == null; i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (IN_LINE.equalsIgnoreCase(n.getNodeName())) {
				oResult = parseInLine(n);
			} else if (WRAPPER.equalsIgnoreCase(n.getNodeName())) {
				oResult = parseWrapper(n);
			} else {
				mLogger.log(Level.WARNING, "Found node \"" + n.getNodeName() + "\" (type=" + n.getNodeType() + ") instead of <" + IN_LINE + "> or <" + WRAPPER + ">.");
			}
				
		}
		if (oResult == null) {
			mLogger.log(Level.SEVERE, "Invalid VAST 2.0 document. Expecting \"" + IN_LINE + "\" or \"" + WRAPPER  + "\" in <Ad>.");
			throw new InvalidDocumentException(null, new String[] { IN_LINE, WRAPPER });
		}
		oResult.setId(oId);
		return oResult;
	}

	private Ad parseWrapper(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing wrapper...");
		Wrapper oResult = new Wrapper();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (AD_SYSTEM.equalsIgnoreCase(n.getNodeName())) {
				AdSystem oAdSys = parseAdSystem(n);
				oResult.setAdSystem(oAdSys);
			} else if (VAST_AD_TAG_URI.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting vast ad tag uri...");
				oResult.setVASTAdTagURI(n.getNodeValue());
			} else if (ERROR.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting error...");
				oResult.setError(n.getNodeValue());
			} else if (IMPRESSION.equalsIgnoreCase(n.getNodeName())) {
				Impression oImp = parseImpression(n);
				oResult.getImpressions().add(oImp);
			} else if (CREATIVES.equalsIgnoreCase(n.getNodeName())) {
				oResult.setCreatives(parseCreatives(n));
			} else if (EXTENSIONS.equalsIgnoreCase(n.getNodeName())) {
				//TODO
			} else {
				mLogger.log(Level.WARNING, "Wrapper not supported: \"" + n.getNodeName() + "\". Skipped.");
			}
		}
		return oResult;
	}

	private AdSystem parseAdSystem(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing AdSystem...");
		AdSystem oAdSys = new AdSystem();
		oAdSys.setVersion(getAttribute(pNode, VERSION, null));
		oAdSys.setName(pNode.getNodeValue());
		return oAdSys;
	}

	private InLine parseInLine(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing Inline...");
		InLine oResult = new InLine();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (AD_SYSTEM.equalsIgnoreCase(n.getNodeName())) {
				AdSystem oAdSys = parseAdSystem(n);
				mLogger.log(Level.FINEST, "##### AdSystem parsed.");
				oResult.setAdSystem(oAdSys);
			} else if (AD_TITLE.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting ad title...");
				oResult.setAdTitle(n.getNodeValue());
				mLogger.log(Level.FINEST, "### Got ad title.");
			} else if (DESCRIPTION.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting description...");
				oResult.setDescription(n.getNodeValue());
			} else if (SURVEY.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting survey...");
				oResult.setSurvey(n.getNodeValue());
			} else if (ERROR.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting error...");
				oResult.setError(n.getNodeValue());
			} else if (IMPRESSION.equalsIgnoreCase(n.getNodeName())) {
				Impression oImp = parseImpression(n);
				oResult.getImpressions().add(oImp);
			} else if (CREATIVES.equalsIgnoreCase(n.getNodeName())) {
				oResult.setCreatives(parseCreatives(n));
			} else if (EXTENSIONS.equalsIgnoreCase(n.getNodeName())) {
				//TODO
			} else {
				mLogger.log(Level.WARNING, "InLine not supported: \"" + n.getNodeName() + "\". Skipped.");
			}
		}
		return oResult;
	}

	private List<Creative> parseCreatives(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing creatives...");
		List<Creative> oResult = new ArrayList<Creative>();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			Creative c = parseCreative(n);
			oResult.add(c);
		}
		return oResult;
	}

	private Creative parseCreative(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing creative...");
		Creative oResult = null;
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength() && oResult == null; i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (LINEAR.equalsIgnoreCase(n.getNodeName())) {
				oResult = parseLinear(n);
			} else if (COMPANION_ADS.equalsIgnoreCase(n.getNodeName())) {
				oResult = parseCompanionAds(n);
			} else if (NON_LINEAR_ADS.equalsIgnoreCase(n.getNodeName())) {
				oResult = parseNonLinearAds(n);
			} else {
				mLogger.log(Level.WARNING, "Found node \"" + n.getNodeName() + "\" (type=" + n.getNodeType() + ") instead of <" + LINEAR + "> or <" + COMPANION_ADS + "> or <" + NON_LINEAR_ADS + ">.");
			}
			if (oResult != null) {
				oResult.setAdId(getAttribute(n, AD_ID, null));
				oResult.setId(getAttribute(n, ID, null));
				oResult.setSequence(getAttribute(n, SEQUENCE, 1));
			}
		}
		
		if (oResult == null) {
			mLogger.log(Level.SEVERE, "Invalid VAST 2.0 document.Expecting \"" + LINEAR + "\" or \"" + COMPANION_ADS + "\" or \"" + NON_LINEAR_ADS + "\".");
			throw new InvalidDocumentException(null, new String[] { LINEAR, COMPANION_ADS, NON_LINEAR_ADS });
		}
		
		return oResult;
	}

	private Creative parseNonLinearAds(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing non-linear ads...");
		NonLinearAds oResult = new NonLinearAds();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (NON_LINEAR.equalsIgnoreCase(n.getNodeName())) {
				NonLinearAd oAd = parseNonLinear(n);
				oResult.getList().add(oAd);
			} else if (TRACKING_EVENTS.equalsIgnoreCase(n.getNodeName())) {
				List<Tracking> oTrackingEvents = parseTrackingEvents(n);
				oResult.setTrackingEvents(oTrackingEvents);				
			} else {
				mLogger.log(Level.WARNING, "NonLinear child not supported: \"" + n.getNodeName() + "\". Skipping.");
			}
		}
		return oResult; 
	}

	private Creative parseCompanionAds(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing companion ads...");
		CompanionAds oResult = new CompanionAds();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			CompanionAd oCompanion = parseCompanionAd(n);
			oResult.getCompanions().add(oCompanion);
		}
		return oResult;
	}
	
	private CompanionAd parseCompanionAd(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing companion ad...");
		CompanionAd oResult = new CompanionAd();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (AD_PARAMETERS.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting ad parameters (companion ad)...");
				oResult.setAdParameters(n.getNodeValue());
			} else if (COMPANION_CLICK_THROUGH.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting companion click through (companion ad)...");
				oResult.setClickThrough(n.getNodeValue());
			} else if (ALT_TEXT.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting alt text (companion ad)...");
				oResult.setAltText(n.getNodeValue());
			} else if (STATIC_RESOURCE.equalsIgnoreCase(n.getNodeName())) {
				oResult.setResource(parseStaticResource(n));
			} else if (HTML_RESOURCE.equalsIgnoreCase(n.getNodeName())) {
				oResult.setResource(parseHTMLResource(n));
			} else if (IFRAME_RESOURCE.equalsIgnoreCase(n.getNodeName())) {
				oResult.setResource(parseIFrameResource(n));
			} else if (TRACKING_EVENTS.equalsIgnoreCase(n.getNodeName())) {
				List<Tracking> oTrackings = parseTrackingEvents(n);
				oResult.setTrackingEvents(oTrackings);				
			} else {
				mLogger.log(Level.WARNING, "NonLinear child not supported: \"" + n.getNodeName() + "\". Skipping.");
			}
		}
		oResult.setId(getAttribute(pNode, ID, null));
		oResult.setHeight(getAttribute(pNode, HEIGHT, 0));
		oResult.setWidth(getAttribute(pNode, WIDTH, 0));
		oResult.setExpandedHeight(getAttribute(pNode, EXPANDED_HEIGHT, 0));
		oResult.setExpandedWidth(getAttribute(pNode, EXPANDED_WIDTH, 0));		
		oResult.setApiFramework(getAttribute(pNode, API_FRAMEWORK, null));
		return oResult;
	}

	private NonLinearAd parseNonLinear(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing non-linear...");
		NonLinearAd oResult = new NonLinearAd();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (AD_PARAMETERS.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting ad parameters (non-linear ad)...");
				oResult.setAdParameters(n.getNodeValue());
			} else if (NON_LINEAR_CLICK_THROUGH.equalsIgnoreCase(n.getNodeName())) {
				oResult.setClickThrough(n.getNodeValue());
			} else if (STATIC_RESOURCE.equalsIgnoreCase(n.getNodeName())) {
				oResult.setResource(parseStaticResource(n));
			} else if (HTML_RESOURCE.equalsIgnoreCase(n.getNodeName())) {
				oResult.setResource(parseHTMLResource(n));
			} else if (IFRAME_RESOURCE.equalsIgnoreCase(n.getNodeName())) {
				oResult.setResource(parseIFrameResource(n));
			} else {
				mLogger.log(Level.WARNING, "NonLinear child not supported: \"" + n.getNodeName() + "\". Skipping.");
			}
		}
		oResult.setApiFramework(getAttribute(pNode, API_FRAMEWORK, null));
		oResult.setExpandedHeight(getAttribute(pNode, EXPANDED_HEIGHT, 0));
		oResult.setExpandedWidth(getAttribute(pNode, EXPANDED_WIDTH, 0));
		oResult.setHeight(getAttribute(pNode, HEIGHT, 0));
		oResult.setWidth(getAttribute(pNode, WIDTH, 0));
		oResult.setId(getAttribute(pNode, ID, null));
		oResult.setMaintainAspectRatio(getAttribute(pNode, MAINTAIN_ASPECT_RATIO, false)); // TODO: default???
		oResult.setMinSuggestedDuration(parseTimeToSeconds(getAttribute(pNode, MIN_SUGGESTED_DURATION, "00:00:00"))); // TODO: default????
		oResult.setScalable(getAttribute(pNode, SCALABLE, false));
		return oResult;
	}

	private CompanionResource parseIFrameResource(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing IFrame resource...");
		CompanionResource oResult = new CompanionResource();
		oResult.setType(CompanionResourceType.IFrame);
		String oUri = getFirstNodeValue(pNode);
		if (oUri != null) {
			oResult.setURI(oUri);
		} else {
			mLogger.log(Level.WARNING, "Could not get uri for iframe resource.");
		}
		return oResult;
	}

	private CompanionResource parseHTMLResource(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing HTML resource...");
		CompanionResource oResult = new CompanionResource();
		oResult.setType(CompanionResourceType.HTML);
		String oContent = getFirstNodeValue(pNode);
		if (oContent != null) {
			oResult.setContent(oContent);
		} else {
			mLogger.log(Level.WARNING, "Could not get content for HTML resource.");
		}
		return oResult;
	}

	private CompanionResource parseStaticResource(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing static resource...");
		CompanionResource oResult = new CompanionResource();
		//System.out.println("################################################## Static Resource !");
		//System.out.println("Node : " + pNode.getNodeName() + ", type = " + pNode.getNodeType() + ", value = " + pNode.getNodeValue());
		oResult.setType(CompanionResourceType.Static);
		String oUri = getFirstNodeValue(pNode);
		if (oUri != null) {
			oResult.setURI(oUri);
		} else {
			mLogger.log(Level.WARNING, "Could not get uri for static resource.");
		}
		oResult.setCreativeType(getAttribute(pNode, CREATIVE_TYPE, "")); // TODO: default???
		return oResult;
	}

	private Creative parseLinear(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing linear...");
		Linear oLinear = new Linear();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (DURATION.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting duration...");
				int oSeconds = parseTimeToSeconds(n.getNodeValue());
				oLinear.setDurationInSeconds(oSeconds);
			} else if (TRACKING_EVENTS.equalsIgnoreCase(n.getNodeName())) {
				List<Tracking> oTE = parseTrackingEvents(n);
				oLinear.setTrackingEvents(oTE);
			} else if (AD_PARAMETERS.equalsIgnoreCase(n.getNodeName())) {
				mLogger.log(Level.FINEST, "Getting ad parameters...");
				oLinear.setAdParameters(n.getNodeValue());
			} else if (VIDEO_CLICKS.equalsIgnoreCase(n.getNodeName())) {
				VideoClicks oClicks = parseVideoClicks(n);
				oLinear.setVideoClicks(oClicks);
			} else if (MEDIA_FILES.equalsIgnoreCase(n.getNodeName())) {
				List<MediaFile> oMediaFiles = parseMediaFiles(n);
				oLinear.setMediaFiles(oMediaFiles);
			} else {
				mLogger.log(Level.WARNING, "Linear attribute not supported: " + n.getNodeName() + ". Skipped.");
			}
		}
		return oLinear;
	}

	private List<MediaFile> parseMediaFiles(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing media files...");
		List<MediaFile> oResult = new ArrayList<MediaFile>();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			MediaFile m = new MediaFile();
			m.setApiFramework(getAttribute(n, API_FRAMEWORK, null));
			m.setBitrate(getAttribute(n, BITRATE, 0));
			m.setWidth(getAttribute(n, WIDTH, 0));
			m.setHeight(getAttribute(n, HEIGHT, 0));
			m.setId(getAttribute(n, ID, null));
			m.setType(getAttribute(n, TYPE, null));
			m.setScalable(getAttribute(n, SCALABLE, false));
			m.setMaintainAspectRatio(getAttribute(n, MAINTAIN_ASPECT_RATIO, false));
			m.setDelivery(Delivery.parse(getAttribute(n, DELIVERY, PROGRESSIVE)));
			String oUri = getFirstNodeValue(n);
			if (oUri != null) {
				m.setURI(oUri);
			} else {
				mLogger.log(Level.WARNING, "Could not get uri for media file.");
			}
			oResult.add(m);
		}
		return oResult;
	}

	private VideoClicks parseVideoClicks(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing video clicks...");
		VideoClicks oResult = new VideoClicks();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			IdURI oIdURI = new IdURI();
			String oUri = getFirstNodeValue(n);
			if (oUri != null) {
				oIdURI.setURI(oUri);
			} else {
				mLogger.log(Level.WARNING, "Could not get uri for video click.");
			}
			oIdURI.setId(getAttribute(n, ID, null));
			if (CLICK_THROUGH.equalsIgnoreCase(n.getNodeName())) {
				oResult.setClickThrough(oIdURI);
			} else if (CLICK_TRACKING.equalsIgnoreCase(n.getNodeName())) {
				oResult.getClickTrackings().add(oIdURI);
			} else if (CUSTOM_CLICK.equalsIgnoreCase(n.getNodeName())) {
				oResult.getCustomClicks().add(oIdURI);
			} else {
				mLogger.log(Level.WARNING, "Video Click not supported: " + n.getNodeName() + ". Skipped.");
			}
		}
		return oResult;
	}

	private List<Tracking> parseTrackingEvents(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing tracking events...");
		List<Tracking> oResult = new ArrayList<Tracking>();
		NodeList oChildren = pNode.getChildNodes();
		for (int i = 0; i < oChildren.getLength(); i++) {
			Node n = oChildren.item(i);
			if (n.getNodeType() != Node.ELEMENT_NODE) continue;
			if (TRACKING.equalsIgnoreCase(n.getNodeName())) {
				Tracking t = new Tracking();
				t.setURI(n.getNodeValue());
				t.setEvent(TrackingEvent.parse(getAttribute(n, EVENT, CREATIVE_VIEW)));
				oResult.add(t);
			} else {
				mLogger.log(Level.WARNING, "Tracking not supported: " + n.getNodeName() + ". Skipped.");
			}
		}
		return oResult;
	}

	private int parseTimeToSeconds(String pValue) {
		if (pValue == null || pValue.isEmpty()) return 0;
		String[] oTimes = pValue.split(COLON);
		switch (oTimes.length) {
		case 0:
			return 0;
		case 1:
			return Integer.parseInt(oTimes[0]);
		case 2:
			return Integer.parseInt(oTimes[0]) * 60
					+ Integer.parseInt(oTimes[1]);
		case 3:
			return Integer.parseInt(oTimes[0]) * 3600
					+ Integer.parseInt(oTimes[1]) * 60
					+ Integer.parseInt(oTimes[2]);
		}
		return -1;
	}

	private Impression parseImpression(Node pNode) {
		mLogger.log(Level.FINEST, "Parsing impression...");
		Impression oResult = new Impression();
		oResult.setId(getAttribute(pNode, ID, null));
		String oUri = getFirstNodeValue(pNode);
		if (oUri != null) {
			oResult.setURI(oUri);
		} else {
			mLogger.log(Level.WARNING, "Could not get Uri for impression.");
		}
		return oResult;
	}

	private String getFirstNodeValue(Node pNode) {
		if (pNode.getNodeValue() != null) return pNode.getNodeValue();
		NodeList oChildren = pNode.getChildNodes();
		String oResult = null;
		for (int i = 0; i < oChildren.getLength() && oResult == null; i++) {
			Node n = oChildren.item(i);
			oResult = n.getNodeValue();
		}
		return oResult;
	}

	private String getAttribute(Node pNode, String pName, String pDefault) {
		Node n = pNode.getAttributes().getNamedItem(pName);
		if (n == null)
			return pDefault;
		return n.getNodeValue();
	}

	private int getAttribute(Node pNode, String pName, int pDefault) {
		Node n = pNode.getAttributes().getNamedItem(pName);
		if (n == null)
			return pDefault;
		return Integer.parseInt(n.getNodeValue());
	}

	private boolean getAttribute(Node pNode, String pName, boolean pDefault) {
		Node n = pNode.getAttributes().getNamedItem(pName);
		if (n == null)
			return pDefault;
		return Boolean.parseBoolean(n.getNodeValue());
	}
}
