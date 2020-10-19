import Vue from 'vue'
import VuetifyToast from 'vuetify-toast-snackbar'
import Vuetify, { VIcon, VSnackbar } from 'vuetify/lib'
import 'vuetify/src/styles/main.sass'

Vue.use(Vuetify, {
  components: {
    // the components which Toast.vue used
    VIcon,
    VSnackbar
  }
})

Vue.use(VuetifyToast, {
    x: 'center', // default
    y: 'top', // default
    color: 'info', // default
    icon: 'info',
    timeout: 3000, // default
    dismissable: true, // default
    autoHeight: false, // default
    multiLine: false, // default
    vertical: false, // default
    shorts: {
        custom: {
            color: 'purple'
        },
        success: {
            color: 'success'
        },
        error: {
            color: 'error'
        }
    },
    property: '$toast' // default
});