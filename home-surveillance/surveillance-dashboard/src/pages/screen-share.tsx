import React from "react";

function getDisplayMedia(
  constraints: MediaStreamConstraints
): Promise<MediaStream> {
  if (
    navigator.mediaDevices &&
    (navigator.mediaDevices as any).getDisplayMedia
  ) {
    return (navigator.mediaDevices as any).getDisplayMedia(constraints);
  } else {
    return navigator.getDisplayMedia(constraints);
  }
}

class ScreenShare extends React.Component {
  state = {
    streamEnabled: false,
    error: undefined
  };

  private video = React.createRef<HTMLVideoElement>();

  componentDidMount() {
    this.initializeStream();
  }

  componentWillUnmount() {
    if (this.video.current !== null && this.video.current.srcObject !== null) {
      const mediaStream = this.video.current.srcObject as MediaStream;

      const videoTracks = mediaStream.getVideoTracks();
      videoTracks.forEach(track => {
        track.stop();
      });

      this.video.current.srcObject = null;
      this.setState({ streamEnabled: false, error: undefined });
    }
  }

  getMediaConstraints = () => {
    return { video: true };
  };

  initializeStream = async () => {
    const mediaConstraints = this.getMediaConstraints();

    try {
      const displayMedia = await getDisplayMedia(mediaConstraints);

      if (this.video.current) {
        this.video.current.srcObject = displayMedia;
        this.setState({ streamEnabled: true });

        const configuration: RTCConfiguration = {
          iceServers: [{ urls: "localhost" }]
        };

        const connection = new RTCPeerConnection(configuration);

        displayMedia
          .getTracks()
          .forEach(track => connection.addTrack(track, displayMedia));
      }
    } catch (e) {
      this.setState({ error: "Unable to acquire screen capture" });
    }
  };

  render() {
    return (
      <div>
        {this.state.streamEnabled ? "Live" : "Offline"}
        <video autoPlay playsInline ref={this.video} />
        {this.state.error && <div>{this.state.error}</div>}
      </div>
    );
  }
}

export default ScreenShare;
