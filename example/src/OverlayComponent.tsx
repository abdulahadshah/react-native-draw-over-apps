import React from 'react';
import { View, Text, type StyleProp, type ViewStyle } from 'react-native';

const OverlayComponent = () => {
  const overlayStyle: StyleProp<ViewStyle> = {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  };

  return (
    <View style={overlayStyle}>
      <Text>Hello from Overlay!</Text>
    </View>
  );
};

export default OverlayComponent;
