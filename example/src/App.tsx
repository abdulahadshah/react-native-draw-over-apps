import * as React from 'react';

import { Button, StyleSheet, View } from 'react-native';
import { useDrawOverApps, askForPermission } from 'react-native-draw-over-apps';

export default function App() {
  const { createOverlay } = useDrawOverApps();

  async function createOverlayWithPermissions() {
    try {
      const isGranted = await askForPermission();
      if (isGranted) {
        createOverlay();
      }
    } catch (e) {
      console.log(e);
    }
  }

  return (
    <View style={styles.container}>
      <Button title="Create Overlay" onPress={createOverlayWithPermissions} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
