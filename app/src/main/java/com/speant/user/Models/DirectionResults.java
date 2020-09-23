package com.speant.user.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DirectionResults {

    @SerializedName("routes")
    private List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public class Route {
        @SerializedName("overview_polyline")
        private OverviewPolyLine overviewPolyLine;



        private List<Legs> legs;

        public OverviewPolyLine getOverviewPolyLine() {
            return overviewPolyLine;
        }

        public class OverviewPolyLine {

            @SerializedName("points")
            public String points;

            public String getPoints() {
                return points;
            }
        }

        public List<Legs> getLegs() {
            return legs;
        }

        public class Legs {
            private List<Steps> steps;
            @SerializedName("distance")
            private Distance distance;
            @SerializedName("duration")
            private Duration duration;

            public List<Steps> getSteps() {
                return steps;
            }

            public Distance getDistance() {
                return distance;
            }

            public Duration getDuration() {
                return duration;
            }

            public class Distance {

                @SerializedName("text")
                @Expose
                private String text;
                @SerializedName("value")
                @Expose
                private Integer value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public Integer getValue() {
                    return value;
                }

                public void setValue(Integer value) {
                    this.value = value;
                }

            }

            public class Duration {

                @SerializedName("text")
                @Expose
                private String text;
                @SerializedName("value")
                @Expose
                private Integer value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public Integer getValue() {
                    return value;
                }

                public void setValue(Integer value) {
                    this.value = value;
                }

            }
        }

        public class Steps {
            private Location start_location;
            private Location end_location;
            private OverviewPolyLine polyline;

            public Location getStart_location() {
                return start_location;
            }

            public Location getEnd_location() {
                return end_location;
            }

            public OverviewPolyLine getPolyline() {
                return polyline;
            }


            public class Location {
                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }
            }
        }
    }
}
