module.exports = {
  devServer: {
      port: 80,
      proxy: {
          '/api': {
              target: 'http://114.115.164.58:8080',
              changeOrigin: true,
              ws: true,
              pathRewrite: {
                '^/api': ''
              }
          },

      },
      disableHostCheck: true,
  },
  transpileDependencies: ['vuetify'],

  pluginOptions: {
    i18n: {
      locale: 'en',
      fallbackLocale: 'en',
      localeDir: 'locales',
      enableInSFC: false,
    },
  },
}
