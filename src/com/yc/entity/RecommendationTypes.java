package com.yc.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
@DiscriminatorValue("recommendationTypes")
public class RecommendationTypes {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer recommendTypesID;
	
	@Column
    @Enumerated(EnumType.STRING)
    private RecommendationType type ;

	@Column
	private String recommendationName;
	
	@OneToMany(mappedBy = "recommendationTypes",cascade={CascadeType.REFRESH,CascadeType.MERGE})
	private List<Recommendation> RecommendationResults;

	public List<Recommendation> getRecommendationResults() {
		return RecommendationResults;
	}

	public void setRecommendationResults(List<Recommendation> recommendationResults) {
		RecommendationResults = recommendationResults;
	}

	public Integer getRecommendTypesID() {
		return recommendTypesID;
	}

	public void setRecommendTypesID(Integer recommendTypesID) {
		this.recommendTypesID = recommendTypesID;
	}

	public RecommendationType getType() {
		return type;
	}

	public void setType(RecommendationType type) {
		this.type = type;
	}

	public String getRecommendationName() {
		return recommendationName;
	}

	public void setRecommendationName(String recommendationName) {
		this.recommendationName = recommendationName;
	}
	
}
