package com.socialone.android.jinstagram.entity.comments;

import com.google.gson.annotations.SerializedName;
import com.socialone.android.jinstagram.InstagramObject;
import com.socialone.android.jinstagram.entity.common.Meta;


public class MediaCommentResponse extends InstagramObject {
	@SerializedName("data")
	private CommentData commentData;

	@SerializedName("meta")
	private Meta meta;

	/**
	 * @return the commentData
	 */
	public CommentData getCommentData() {
		return commentData;
	}

	/**
	 * @param commentData the commentData to set
	 */
	public void setCommentData(CommentData commentData) {
		this.commentData = commentData;
	}

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

    @Override
    public String toString() {
        return String.format("MediaCommentResponse [commentData=%s, meta=%s]", commentData, meta);
    }
}
