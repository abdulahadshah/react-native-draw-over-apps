import { AppRegistry } from 'react-native';
import App from './src/App';
import { name as appName } from './app.json';
import OverlayComponent from './src/OverlayComponent';

AppRegistry.registerComponent(appName, () => App);
// Register the component
AppRegistry.registerComponent('OverlayComponent', () => OverlayComponent);
