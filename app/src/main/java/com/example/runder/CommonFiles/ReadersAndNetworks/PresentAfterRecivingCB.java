package com.example.runder.CommonFiles.ReadersAndNetworks;

import com.example.runder.Models.RunnersModelFull;
import com.google.android.gms.maps.model.LatLng;

public interface PresentAfterRecivingCB {
    public void PresentAfterReciving(RunnersModelFull runnersModelFull, LatLng latLng);
}
