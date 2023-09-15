import React, { useState } from "react";
import { GoogleMap, useJsApiLoader, Marker } from "@react-google-maps/api";
import useDidMountEffect from "../../../useDidMountEffect";

interface Place {
  name: string;
  geometry: {
    location: {
      lat: number;
      lng: number;
    };
  };
}

const containerStyle = {
  width: "100%",
  height: "95vh",
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
  menu: string;
}

const ValleyMap = ({ latitude, longitude, menu }: Props) => {
  const center = {
    lat: latitude,
    lng: longitude,
  };

  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: `${process.env.REACT_APP_GOOGLE_MAP_KEY}`,
  });
  const [map, setMap] = React.useState(null);
  const [place, setPlace] = useState<any>([]);

  const onLoad = React.useCallback(function callback(map: any) {
    const bounds = new window.google.maps.LatLngBounds(center);
    map.fitBounds(bounds);

    setMap(map);
  }, []);

  const onUnmount = React.useCallback(function callback(map: any) {
    setMap(null);
  }, []);

  useDidMountEffect(() => {
    if (map) {
      if (!window.google) {
        console.error("Google Maps JavaScript API가 아직 로드되지 않았습니다.");
        return;
      }

      const service = new window.google.maps.places.PlacesService(
        document.createElement("div")
      );

      const request = {
        location: center,
        radius: 10000,
        query: menu,
      };

      service.textSearch(request, (results, status) => {
        if (status === window.google.maps.places.PlacesServiceStatus.OK) {
          setPlace(results);
        }
      });
    }
  }, [menu]);

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      center={center}
      onLoad={onLoad}
      onUnmount={onUnmount}
      options={OPTIONS}
      zoom={10}
    >
      <Marker
        position={center}
        icon={{
          url: process.env.PUBLIC_URL + "/img/marker/marker.png",
          scaledSize: new window.google.maps.Size(26, 32),
        }}
      ></Marker>
      {menu !== "계곡위치" &&
        place &&
        place.map((place: Place, index: number) => (
          <Marker
            key={index}
            position={place.geometry.location}
            title={place.name}
            icon={{
              url: process.env.PUBLIC_URL + "/img/marker/hospital-marker.png",
              scaledSize: new window.google.maps.Size(26, 32),
            }}
          />
        ))}
    </GoogleMap>
  ) : (
    <></>
  );
};

export default ValleyMap;
