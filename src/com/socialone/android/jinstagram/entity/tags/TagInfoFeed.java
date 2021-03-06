package com.socialone.android.jinstagram.entity.tags;

import com.google.gson.annotations.SerializedName;
import com.socialone.android.jinstagram.InstagramObject;
import com.socialone.android.jinstagram.entity.common.Meta;


public class TagInfoFeed extends InstagramObject {
	@SerializedName("meta")
	private Meta meta;

	@SerializedName("data")
	private TagInfoData tagInfo;

	/**
	 * @return the meta
	 */
	public Meta getMeta() {
		return meta;
	}

	/**
	 * @param meta the meta to set
	 */
	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public TagInfoData getTagInfo() {
		return tagInfo;
	}

	public void setTagInfo(TagInfoData tagInfo) {
		this.tagInfo = tagInfo;
	}

    @Override
    public String toString() {
        return String.format("TagInfoFeed [meta=%s, tagInfo=%s]", meta, tagInfo);
    }
}
