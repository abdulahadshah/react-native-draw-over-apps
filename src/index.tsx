import { NativeModules, Platform } from 'react-native';

// Define TypeScript interface for your native module methods
interface DrawOverAppsViewType {
  createOverlay(componentName: string): void;
  askForPermission(): Promise<boolean>;
  // Add other method signatures here if needed, for example:
  // removeOverlay(): void;
}

// Linking error message
const LINKING_ERROR =
  `The package 'react-native-draw-over-apps' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

// Attempt to get the native module
const DrawOverAppsView = NativeModules.DrawOverAppsView as DrawOverAppsViewType;

// Check if the native module is linked properly
if (DrawOverAppsView == null) {
  throw new Error(LINKING_ERROR);
}

// Example usage of the createOverlay method
const useDrawOverApps = () => {
  const createOverlay = () => {
    DrawOverAppsView.createOverlay('OverlayComponent');
  };

  // Expose more methods as needed
  return { createOverlay };
};

const askForPermission = () => {
  return DrawOverAppsView.askForPermission();
};

export { useDrawOverApps, askForPermission };
