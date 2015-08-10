package com.yc.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.yc.entity.user.ConstitutionType;

@Entity
@DiscriminatorValue("recommendation")
public class Recommendation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer recommendationID;

	@Column
	private String title;

	@ElementCollection
	@Column(name = "solarterm")
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "recommid_solar")
	private List<SolarTerm> solarTerms = new ArrayList<SolarTerm>();
	
	@ElementCollection
	@Column(name = "fitconstypes")
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "recommid_fitconstype")
	private List<ConstitutionType> fitConsTypes = new ArrayList<ConstitutionType>();
	
	@ElementCollection
	@Column(name = "fitconstypes")
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "recommid_unconstype")
	private List<ConstitutionType> unsuitedConsTypes = new ArrayList<ConstitutionType>();
	
	@Column
	private String describes;
	
	@Column
	private String photoPath;
	
	@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REFRESH}, fetch = FetchType.EAGER)
	@JoinColumn(name = "types_id")
	private RecommendationTypes recommendationTypes;
	
	@OneToOne(mappedBy = "recommendation")
	private Collection collection;

	public Collection getCollection() {
		return collection;
	}

	public void setCollection(Collection collection) {
		this.collection = collection;
	}

	public List<ConstitutionType> getFitConsTypes() {
		return fitConsTypes;
	}

	public void setFitConsTypes(List<ConstitutionType> fitConsTypes) {
		this.fitConsTypes = fitConsTypes;
	}
	
	public void addFitConsTypes(ConstitutionType constitutionType){
		fitConsTypes.add(constitutionType);
	}

	public List<ConstitutionType> getUnsuitedConsTypes() {
		return unsuitedConsTypes;
	}

	public void setUnsuitedConsTypes(List<ConstitutionType> unsuitedConsTypes) {
		this.unsuitedConsTypes = unsuitedConsTypes;
	}
	
	public void addUnsuitedConsTypes(ConstitutionType unsuitedConsType) {
		unsuitedConsTypes.add(unsuitedConsType); 
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public String getDescribes() {
		return describes;
	}

	public void setDescribes(String describes) {
		this.describes = describes;
	}

	public RecommendationTypes getRecommendationTypes() {
		return recommendationTypes;
	}

	public void setRecommendationTypes(RecommendationTypes recommendationTypes) {
		this.recommendationTypes = recommendationTypes;
	}

	public List<SolarTerm> getSolarTerms() {
		return solarTerms;
	}

	public void setSolarTerms(List<SolarTerm> solarTerms) {
		this.solarTerms = solarTerms;
	}
	
	public void addSolarTerms(SolarTerm solarTerm) {
		solarTerms.add(solarTerm);
	}

	public Integer getRecommendationID() {
		return recommendationID;
	}

	public void setRecommendationID(Integer recommendationID) {
		this.recommendationID = recommendationID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
