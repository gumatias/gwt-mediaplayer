/**
 * 
 */
package net.sf.video4j.gwt.plugin.shared.vast;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * @author luc
 * 
 */
public class CompanionResource implements IsSerializable {

    private CompanionResourceType mType;
    private String                mContent;     // HTML
    private String	              mURI;         // Static and IFrame
    private String                mCreativeType; // Static

    public CompanionResourceType getType() {
        return mType;
    }

    public void setType(CompanionResourceType pType) {
        mType = pType;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String pContent) {
        mContent = pContent;
    }

    public String getURI() {
        return mURI;
    }

    public void setURI(String pURI) {
        mURI = pURI;
    }

    public String getCreativeType() {
        return mCreativeType;
    }

    public void setCreativeType(String pCreativeType) {
        mCreativeType = pCreativeType;
    }

    @Override
    public String toString() {
        return "CompanionResource [mType=" + mType + ", mContent=" + mContent
                + ", mURI=" + mURI + ", mCreativeType=" + mCreativeType + "]";
    }
}
