import React, { useEffect, useState } from "react";
import {
  GoogleMap,
  useJsApiLoader,
  Marker,
  InfoWindow,
} from "@react-google-maps/api";
import useDidMountEffect from "../../../useDidMountEffect";
import RatingStar from "../../common/RatingStar";
import styled from "styled-components";

interface Place {
  name: string;
  geometry: {
    location: {
      lat: number;
      lng: number;
    };
  };
  formatted_address: string;
  rating: number;
  user_ratings_total: number;
}

const containerStyle = {
  width: "100%",
  height: "40em",
  borderRadius: "10px",
};

const responsiveStyle = {
  width: "100%",
  height: "20em",
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
  minZoom: 5,
  maxZoom: 13,
};

interface Props {
  latitude: number;
  longitude: number;
  menu: string;
}

const InfoWindowCustom = styled.div`
  div {
    display: flex;
    margin-bottom: 0.3em;

    span:first-child {
      font-weight: bold;
      margin-right: 0.3em;
      font-size: 1rem;
    }

    span:last-child,
    span:nth-child(2) {
      margin: 0 0.5em 0 0.5em;
      font-size: 0.8rem;
      color: #717171;
    }
  }

  span:last-child {
    font-size: 0.8rem;
  }
`;

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
  const [selectedMarker, setSelectedMarker] = useState<Place>();
  const [innerWidth, setInnerWidth] = useState(window.innerWidth);

  useEffect(() => {
    const resizeListener = () => {
      setInnerWidth(window.innerWidth);
    };
    window.addEventListener("resize", resizeListener);
  });

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
      mapContainerStyle={innerWidth <= 1260 ? responsiveStyle : containerStyle}
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
            onClick={() => {
              setSelectedMarker(place);
            }}
          />
        ))}
      {menu !== "계곡위치" && place && selectedMarker && (
        <InfoWindow
          position={selectedMarker.geometry.location}
          onCloseClick={() => setSelectedMarker(undefined)}
          options={{
            pixelOffset: new window.google.maps.Size(0, -25),
          }}
        >
          <InfoWindowCustom>
            <div>
              <span>{selectedMarker.name}</span>
              <span>{selectedMarker.rating}</span>
              <RatingStar rating={selectedMarker.rating} size="15px" />
              <span>({selectedMarker.user_ratings_total})</span>
            </div>
            <span>{selectedMarker.formatted_address}</span>
          </InfoWindowCustom>
        </InfoWindow>
      )}
    </GoogleMap>
  ) : (
    <></>
  );
};

export default ValleyMap;
