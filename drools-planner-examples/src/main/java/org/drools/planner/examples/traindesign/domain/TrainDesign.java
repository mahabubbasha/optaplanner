/*
 * Copyright 2010 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.planner.examples.traindesign.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.drools.planner.api.domain.solution.PlanningEntityCollectionProperty;
import org.drools.planner.core.score.HardAndSoftScore;
import org.drools.planner.core.solution.Solution;
import org.drools.planner.examples.common.domain.AbstractPersistable;
import org.drools.planner.examples.pas.domain.BedDesignation;

@XStreamAlias("TrainDesign")
public class TrainDesign extends AbstractPersistable implements Solution<HardAndSoftScore> {

    private TrainDesignParametrization trainDesignParametrization;

    private List<RailNode> railNodeList;
    private List<RailArc> railArcList;
    private List<CarBlock> carBlockList;
    private List<CrewSegment> crewSegmentList;

    private List<BedDesignation> bedDesignationList; // TODO replace me

    private HardAndSoftScore score;

    public TrainDesignParametrization getTrainDesignParametrization() {
        return trainDesignParametrization;
    }

    public void setTrainDesignParametrization(TrainDesignParametrization trainDesignParametrization) {
        this.trainDesignParametrization = trainDesignParametrization;
    }

    public List<RailNode> getRailNodeList() {
        return railNodeList;
    }

    public void setRailNodeList(List<RailNode> railNodeList) {
        this.railNodeList = railNodeList;
    }

    public List<RailArc> getRailArcList() {
        return railArcList;
    }

    public void setRailArcList(List<RailArc> railArcList) {
        this.railArcList = railArcList;
    }

    public List<CarBlock> getCarBlockList() {
        return carBlockList;
    }

    public void setCarBlockList(List<CarBlock> carBlockList) {
        this.carBlockList = carBlockList;
    }

    public List<CrewSegment> getCrewSegmentList() {
        return crewSegmentList;
    }

    public void setCrewSegmentList(List<CrewSegment> crewSegmentList) {
        this.crewSegmentList = crewSegmentList;
    }

    @PlanningEntityCollectionProperty
    public List<BedDesignation> getBedDesignationList() {
        return bedDesignationList;
    }

    public void setBedDesignationList(List<BedDesignation> bedDesignationList) {
        this.bedDesignationList = bedDesignationList;
    }

    public HardAndSoftScore getScore() {
        return score;
    }

    public void setScore(HardAndSoftScore score) {
        this.score = score;
    }

    public Collection<? extends Object> getProblemFacts() {
        List<Object> facts = new ArrayList<Object>();
        facts.add(trainDesignParametrization);
        facts.addAll(railNodeList);
        facts.addAll(railArcList);
        facts.addAll(carBlockList);
        facts.addAll(crewSegmentList);
//        facts.addAll(calculateAdmissionPartSpecialismMissingInRoomList());
        // Do not add the planning entity's (bedDesignationList) because that will be done automatically
        return facts;
    }

    /**
     * Clone will only deep copy the {@link #bedDesignationList}.
     */
    public TrainDesign cloneSolution() {
        TrainDesign clone = new TrainDesign();
        clone.id = id;
        clone.trainDesignParametrization = trainDesignParametrization;
        clone.railNodeList = railNodeList;
        clone.railArcList = railArcList;
        clone.carBlockList = carBlockList;
        clone.crewSegmentList = crewSegmentList;
        List<BedDesignation> clonedBedDesignationList = new ArrayList<BedDesignation>(bedDesignationList.size());
        for (BedDesignation bedDesignation : bedDesignationList) {
            BedDesignation clonedBedDesignation = bedDesignation.clone();
            clonedBedDesignationList.add(clonedBedDesignation);
        }
        clone.bedDesignationList = clonedBedDesignationList;
        clone.score = score;
        return clone;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (id == null || !(o instanceof TrainDesign)) {
            return false;
        } else {
            TrainDesign other = (TrainDesign) o;
            if (bedDesignationList.size() != other.bedDesignationList.size()) {
                return false;
            }
            for (Iterator<BedDesignation> it = bedDesignationList.iterator(), otherIt = other.bedDesignationList.iterator(); it.hasNext();) {
                BedDesignation bedDesignation = it.next();
                BedDesignation otherBedDesignation = otherIt.next();
                // Notice: we don't use equals()
                if (!bedDesignation.solutionEquals(otherBedDesignation)) {
                    return false;
                }
            }
            return true;
        }
    }

    public int hashCode() {
        HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
        for (BedDesignation bedDesignation : bedDesignationList) {
            // Notice: we don't use hashCode()
            hashCodeBuilder.append(bedDesignation.solutionHashCode());
        }
        return hashCodeBuilder.toHashCode();
    }

    public void initializeTransientProperties() {
        for (RailNode origin : railNodeList) {
            origin.initializeShortestPathMap(railNodeList);
        }
        for (CrewSegment crewSegment : crewSegmentList) {
            crewSegment.initializeShortestPath();
        }
    }

}
