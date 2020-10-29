<template>
  <div id="base_login">
    <v-app id="container">
    <v-form :model="loginForm"
             :rules="rules"
             class="login_container"
             label-position="left"
             label-width="0px"
             ref="loginForm">
      <p class="login_title">Login</p>
        <v-text-field type="text"
                  prop="username"
                  v-model="loginForm.username"
                  auto-complete="off"
                  label="username"
                  :rules="[rules.username]"
                  :validate-on-blur="true"
                  prepend-icon="account_circle"
                  ></v-text-field>
        <v-text-field type="password"
                  prop="password"
                  v-model="loginForm.password"
                  auto-complete="off"
                  label="password"
                  :rules="[rules.password]"
                  :validate-on-blur="true"
                  prepend-icon="lock"
                  ></v-text-field>
        <v-btn  id="login" style="width: 50%;border: none"
                   v-on:click="login">login</v-btn>
        <router-link to="register">
          <div id="register">Do not have an account? Register now!</div>
        </router-link>
    </v-form>
    </v-app>
  </div>
</template>

<script>
export default {
  name: 'Login',
  data () {
    return {
      loginForm: {
        username: '',
        password: '',
      },
      rules: {
        username: function(username){
          if(!username || username=='')return "please enter username";
          return true;
        },
        password: function(password){
          if(!password || password=='')return "please enter password";
          return true;
        },
      },
    }
  },
  methods: {
    login () {
      if(!this.$refs.loginForm.validate())return false
      this.$axios.post('api/login', {
        username: this.loginForm.username,
        password: this.loginForm.password
      })
        .then(resp => {
          if (resp.status === 200) {
            if(resp.data.responseMessage === "success"){
              this.$toast('successful login!',{color:'green'})
              this.$store.commit('login', resp.data)
             window.setTimeout(function(){
               if(this.loginForm.username == 'admin')
                 this.$router.replace('dashboardAdmin')
               else
                this.$router.replace('dashboard')
              }.bind(this), 2000)
            }
          } else{
            this.$toast('login error due to'  + resp.data.responseBody.reason,{color:'red'})
          }
        })
        .catch(error => {
          this.$toast('login error due to ' + error.responseBody.reason,{color:'red'})
        })
    }
  }
}
</script>

<style scoped>
  #container{
    background: url("../assets/bg2.png");
    background-size: cover;
    background-position: center;
  }
  #base_login{
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    /* position: fixed; */
    margin: auto;
  }
  body{
    margin: 0px;
    padding: 0px;
  }
  .login_container{
    border-radius: 15px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 400px;
    padding: 35px 35px 15px 35px;
    background-color: #fff;
    border: 1px solid #eaeaea;
    /* box-shadow: 5px 5px 10px #cac6c6; */
    opacity: 0.85;
  }
  .login_title {
    margin: 0px;
    margin-bottom: 30px;
    text-align: center;
    color: #505458;
    font-size: 30px;
    /* font-family: 'Times New Roman', Times, serif */
  }

  .input_control{
    font-size: 100px;
    background-color: #000;

  }

  #register {
    text-align: center;
    margin-top: 10px;
    font-size: 5px;
    color: rgb(117, 117, 117);
    /* font-family: "helvetica"; */
  }

  a{
    text-decoration: none;
  }

  .router-link-active{
    text-decoration: none;
  }

  #login{
    margin: 10px 25%;
    background-color: rgb(97, 116, 221);
    color: rgb(225, 242, 255);
  }
</style>
