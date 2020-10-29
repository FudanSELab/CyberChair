<template>
  <div id="base_register">
    <v-app id="container">
    <v-form :model="registerForm" :rules="rules" class="register_container" label-position="left"
             label-width="0px"  ref="registerForm">
      <p class="register_title">Sign Up</p>
        <v-text-field type="text" v-model="registerForm.username"
                  label="username"
                  hint="5-32 characters consisting of letters/numbers/special characters(-_)"
                  :rules="[rules.usernameValidate]"
                  :validate-on-blur="true"
                  prepend-icon="account_circle"
                  ></v-text-field>

        <v-text-field :type="'password'" v-model="registerForm.password"
                  label="password"
                  hint="6-32 characters  consisting of letters/numbers/special characters(-_), at least two categories above and must not contain your username"
                  :rules="[rules.passwordValidate, rules.notContainUsername]"
                  :validate-on-blur="true"
                  prepend-icon="lock"
                  ></v-text-field>

        <v-text-field :type="'password'" 
                  label="confirm-password"
                  :rules="[rules.confirmPassword]"
                  :validate-on-blur="true"
                  prepend-icon="lock"
                  ></v-text-field>

        <v-text-field type="text" v-model="registerForm.fullname"
                  label="fullname"
                  hint="2-4 words seperated by space"
                  :rules="[rules.fullnameValidate]"
                  :validate-on-blur="true"
                  prepend-icon="person"
                  ></v-text-field>

        <v-text-field type="text" v-model="registerForm.email"
                  label="email-address"
                  :rules="[rules.emailValidate]"
                  :validate-on-blur="true"
                  prepend-icon="mail"
                  ></v-text-field>

        <v-text-field type="text" v-model="registerForm.region"
                  label="region"
                  :rules="[rules.required]"
                  :validate-on-blur="true"
                  prepend-icon="location_on"
                  ></v-text-field>

        <v-text-field type="text" v-model="registerForm.institution"
                  label="institution"
                  :rules="[rules.required]"
                  :validate-on-blur="true"
                  prepend-icon="business"
                  ></v-text-field>

      <div>
        <v-btn id="submit" style="width: 50%;border: none" v-on:click="register(registerForm)">CREATE ACCOUNT</v-btn>
      </div>
      <router-link to="login">
        <div id="login">Already have an account? Login now!</div>
      </router-link>
    </v-form>
    </v-app>
  </div>
</template>

<script>
export default {
  name: 'Register',
  data () {
    
    return {
      registerForm: {
        username: '',
        password: '',
        fullname: '',
        usertype: '',
        region:'',
        email:'',
        institution:'',
      },

      rules: {
        required: value => !!value || 'This field is required.',
        confirmPassword: value => value==this.registerForm.password || "This field should be identical to password",
        notContainUsername: value=> !value.includes(this.registerForm.username)||"password must not contain your username",
        usernameValidate: function (username) {
          //username pattern: 6-20 characters consisting of letters or numbers
          var usernamePattern = /^[A-Za-z\-][A-Za-z\d_\-]{4,31}$/;
          if(usernamePattern.test(username)){ 
            return true;
          }
          else if(/^[\d]/.test(username)){
            return "username begins with letters or special characters(-_)"
          }else{
            return "5-32 characters consisting of letters/numbers/special characters(-_)";
          }
         
          
        },
        passwordValidate: function (password) {
          //password pattern:8-20 at least one Captial. normal letter and a number
          var passwordPattern = /^[a-zA-Z_\d\-]{6,32}$/
    
            var appearance = 0;
            if(/.*[a-zA-Z].*/.test(password)) appearance++;
            if(/.*\d.*/.test(password)) appearance++;
            if(/.*[_\-].*/.test(password)) appearance++;
            if(appearance>=2 && passwordPattern.test(password)) {return true;}
            else {
              return "6-32 characters  consisting of letters/numbers/special characters(-_), at least two categories above and must not contain your username";
            }

        },
        fullnameValidate: function (fullname) {
          //fullname pattern: 2-4 words
          var fullnamePattern = /^([A-Za-z]*( |$)){2,4}$/;
          if(fullnamePattern.test(fullname)){
            return true;
          }else{
            return "2-4 words seperated by space";
          }
        },
        emailValidate: function (emailAddress) {
          var emailPattern = /^([A-Za-z0-9_\-\.])+\@([A-Za-z0-9_\-\.])+\.([A-Za-z]{2,4})$/;
          if(emailPattern.test(emailAddress)){
            return true;
          }else{
            return "Invalid email address";
          }
        }
      },
      loading: false
    }
  },
  methods: {
    register (formName) {
          if(!this.$refs.registerForm.validate())return false
          this.$axios.post('api/register', {
              username: this.registerForm.username,
              password: this.registerForm.password,
              fullname: this.registerForm.fullname,
              region: this.registerForm.region,
              email: this.registerForm.email,
              institution: this.registerForm.institution,
              authorities: [this.registerForm.usertype]
            })
            .then(resp => {
              if(resp.status === 200 ) {
                if(resp.data.responseMessage === "success"){
                  this.$toast('successful registration',{color:'green'})
                  window.setTimeout(function(){
                    this.$router.replace('login')
                  }.bind(this), 2000)
                }
              } else{
                this.$toast('register error due to network problem',{color:'red'})
              }
            })
            .catch(error => {
                this.$toast('register error due to network problem',{color:'red'})
            })
        }
  }
}
</script>

<style scoped>

  #base_register{
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    /* position: fixed; */
  }

  #container {
    background-image: url("../assets/bg2.png") ;
    background-size: cover;
    
    background-position: center;
  }

  
  .register_container{
    border-radius: 15px;
    background-clip: padding-box;
    margin: auto auto;
    width: 400px;
    padding: 35px 35px 15px 35px;
    background-color: #fff;
    border: 1px solid #eaeaea;
    /* box-shadow: 5px 5px 10px #cac6c6; */
    opacity: 0.85;
  }
  .register_title{
    margin: 0px;
    margin-bottom: 30px;
    text-align: center;
    color: #505458;
    font-size: 30px;
    /* font-family: 'Times New Roman', Times, serif; */
  }
  #login {
    text-align: center;
    margin-top: 10px;
    font-size: 5px;
    color: rgb(117, 117, 117);
  }
  
  a{
    text-decoration: none;
  }

  .router-link-active{
    text-decoration: none;
  }

  #submit{
    margin: 10px 25%;
    background-color: rgb(97, 116, 221);
    color: rgb(225, 242, 255);
  }

</style>
