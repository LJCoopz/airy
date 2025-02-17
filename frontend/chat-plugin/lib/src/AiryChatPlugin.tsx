import React, {useState} from 'react';
import {AiryChatPluginConfiguration} from './config';
import './translations';

import Chat from './components/chat';

type AiryChatPluginProps = {
  config: AiryChatPluginConfiguration;
  className?: string;
};

const defaultWidth = 380;
const defaultHeight = 700;

export const AiryChatPlugin = (props: AiryChatPluginProps) => {
  const {config, className} = props;

  const [windowHeight, setWindowHeight] = useState(window.innerHeight);
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);

  const handleResize = () => {
    setWindowHeight(window.innerHeight);
    setWindowWidth(window.innerWidth);
  };

  window.addEventListener('resize', handleResize);

  const customStyle = {
    background: 'transparent',
    width: windowWidth < 420 ? windowWidth : Math.min(config.config?.width ?? defaultWidth, windowWidth),
    height: windowHeight < 700 ? windowHeight : Math.min(config.config?.height ?? defaultHeight, windowHeight),
    ...(config.config?.primaryColor && {
      '--color-airy-blue': config.config?.primaryColor,
      '--color-airy-message-outbound': config.config?.primaryColor,
    }),
    ...(config.config?.accentColor && {
      '--color-airy-accent': config.config?.accentColor,
      '--color-airy-blue-hover': config.config?.accentColor,
      '--color-airy-blue-pressed': config.config?.accentColor,
    }),
    ...(config.config?.outboundMessageColor && {
      '--color-airy-message-outbound': config.config?.outboundMessageColor,
    }),
    ...(config.config?.inboundMessageColor && {
      '--color-airy-message-inbound': config.config?.inboundMessageColor,
    }),
    ...(config.config?.outboundMessageTextColor && {
      '--color-airy-message-text-outbound': config.config?.outboundMessageTextColor,
    }),
    ...(config.config?.inboundMessageTextColor && {
      '--color-airy-message-text-inbound': config.config?.inboundMessageTextColor,
    }),
    ...(config.config?.unreadMessageDotColor && {
      '--color-red-alert': config.config?.unreadMessageDotColor,
    }),
  };

  return (
    <div className={className} style={customStyle}>
      <Chat {...config} />
    </div>
  );
};
