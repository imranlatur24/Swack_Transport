package com.swack.transport.model;

import java.io.Serializable;

public class SliderList implements Serializable {
    private String sliderimg_id;
    private String sliderimg_image;

    public SliderList(String sliderimg_id, String sliderimg_image) {
        this.sliderimg_id = sliderimg_id;
        this.sliderimg_image = sliderimg_image;
    }

    public String getSliderimg_id() {
        return sliderimg_id;
    }

    public void setSliderimg_id(String sliderimg_id) {
        this.sliderimg_id = sliderimg_id;
    }

    public String getSliderimg_image() {
        return sliderimg_image;
    }

    public void setSliderimg_image(String sliderimg_image) {
        this.sliderimg_image = sliderimg_image;
    }
}
