package com.yc.entity;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.yc.entity.user.MembersUser;
//收藏
@Entity
@DiscriminatorValue("collection")
public class Collection {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer collectionID;
	
	@Column
	@Enumerated(EnumType.STRING)
	private CollectionType collectionType = CollectionType.commodity;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private MembersUser user;
	
	@OneToOne
	@JoinColumn(name = "collect_recomm")
	private Recommendation recommendation;

	public Recommendation getRecommendation() {
		return recommendation;
	}

	public void setRecommendation(Recommendation recommendation) {
		this.recommendation = recommendation;
	}

	public CollectionType getCollectionType() {
		return collectionType;
	}

	public void setCollectionType(CollectionType collectionType) {
		this.collectionType = collectionType;
	}

	public Integer getCollectionID() {
		return collectionID;
	}

	public void setCollectionID(Integer collectionID) {
		this.collectionID = collectionID;
	}

	public MembersUser getUser() {
		return user;
	}

	public void setUser(MembersUser user) {
		this.user = user;
	}

}
