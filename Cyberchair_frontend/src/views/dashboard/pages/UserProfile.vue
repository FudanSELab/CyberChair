<template>
  <v-container
    id="user-profile"
    fluid
    tag="section"
  >
    <v-row justify="center">
      <v-col
        cols="12"
        md="8"
      >
        <base-material-card>
          <template v-slot:heading>
            <div class="display-2 font-weight-medium">
              My Profile
            </div>

            <div class="subtitle-1 font-weight-medium">
              personal profile as a academic scholar
            </div>
          </template>

          <v-form :model="userInfo">
            <v-container class="py-0">
              <v-row>

                <v-col
                  cols="12"
                  md="6"
                >
                  <v-text-field
                    v-model="userInfo.region"
                    label="region"
                    class="purple-input"
                    disabled
                  />
                </v-col>


                <v-col
                  cols="12"
                  md="6"
                >
                  <v-text-field
                    label="Institution"
                    v-model="userInfo.institution"
                    disabled
                  />
                </v-col>

                <v-col
                  cols="12"
                  md="6"
                >
                  <v-text-field
                    class="purple-input"
                    v-model="userInfo.username"
                    label="username(ID)"
                    disabled
                  />
                </v-col>

                <v-col
                  cols="12"
                  md="6"
                >
                  <v-text-field
                    label="email address"
                    v-model="userInfo.email"
                    class="purple-input"
                    disabled
                  />
                </v-col>

   

                <v-col
                  cols="12"
                  md="6"
                >
                  <v-text-field
                    v-model="userInfo.fullname"
                    label="fullname"
                    class="purple-input"
                    disabled
                  />
                </v-col>




                <v-col cols="12">
                  <v-textarea
                    class="purple-input"
                    label="About Me"
                    value="Our team produces the most amazing websites.We are errangels and designed for CyberChair essay system. For more information and contact, please email to 18302010047@fudan.edu.cn"
                    disabled
                  />
                </v-col>

                <v-col
                  cols="12"
                  class="text-right"
                >
                  <v-btn
                    color="success"
                    class="mr-0"
                    v-on:click="logout"
                  >
                    Logout
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-form>
        </base-material-card>
      </v-col>

      
    </v-row>
  </v-container>
</template>

<script>
  export default {
    data () {
      return {
        snackbar:false,
        snackbar_color:'success',
        timeout: 2000,
        tips_text: '',
        // user: this.$store.state.resp.responseBody,
        userInfo: {
          username: "william789",
          fullname: "william song",
          email: "1622369663@qq.com",
          region: "ShangHai, China",
          institution: "Fudan University",
        }
      }
    },

    methods: {
      loadProfileData(){
        var username = localStorage.getItem('username');
        var requestUrl = "api/user/userinfo";
        var that = this;

        this.$axios.get(
          requestUrl,{
          // {headers: {token: this.$store.state.token},
           params:  {username: username}})
          .then(resp => {

            if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){

              var userInfoObj = resp.data.responseBody.UserInformation;
              that.userInfo.username = userInfoObj.username;
              that.userInfo.email = userInfoObj.email;
              that.userInfo.fullname = userInfoObj.fullname;
              that.userInfo.region = userInfoObj.region;
              that.userInfo.institution = userInfoObj.institution;
            }else{
                this.tips_text = "errors occurred when getting the information"
                this.$toast(this.tips_text,{color:'red'})
          }
          })
          .catch(error => {
                this.tips_text = "error occurred when loading profile data"
                this.$toast(this.tips_text,{color:'red'})
          })
      },

      logout() {
        this.tips_text = "you are going to loggout";
        this.$toast(this.tips_text,{color:'red'})
        this.$store.commit('logout')
        window.setTimeout(function(){
          this.$router.replace('/login')
        }.bind(this), 2000)
      }
    },

    mounted: function(){
      this.loadProfileData();
    }


  }
</script>
