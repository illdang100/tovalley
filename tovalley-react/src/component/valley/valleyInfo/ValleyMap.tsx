import React from "react";
import { GoogleMap, useJsApiLoader, Marker } from "@react-google-maps/api";

const containerStyle = {
  width: "100%",
  height: "80vh",
  borderRadius: "10px",
};

const myStyles = [
  {
    featureType: "poi",
    elementType: "labels",
    stylers: [{ visibility: "off" }],
  },
];

const OPTIONS = {
  disableDefaultUI: true,
  styles: myStyles,
  minZoom: 13,
  maxZoom: 13,
};

interface Props {
  latitude: number;
  longitude: number;
}

const ValleyMap = ({ latitude, longitude }: Props) => {
  const center = {
    lat: latitude,
    lng: longitude,
  };

  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: `${process.env.REACT_APP_GOOGLE_MAP_KEY}`,
  });

  const [map, setMap] = React.useState(null);

  const onLoad = React.useCallback(function callback(map: any) {
    const bounds = new window.google.maps.LatLngBounds(center);
    map.fitBounds(bounds);

    setMap(map);
  }, []);

  const onUnmount = React.useCallback(function callback(map: any) {
    setMap(null);
  }, []);

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      center={center}
      onLoad={onLoad}
      onUnmount={onUnmount}
      options={OPTIONS}
      zoom={13}
    >
      <Marker position={center}></Marker>
    </GoogleMap>
  ) : (
    <></>
  );
};

export default ValleyMap;
