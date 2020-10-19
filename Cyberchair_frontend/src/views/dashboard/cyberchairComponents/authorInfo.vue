<template>
  <v-form ref="AuthorForm" class="px-0 mx-0">
    <v-col class="">
      <v-list
        color=""
        class="lighten-5 "
        style="background-color: rgb(232, 232, 232)"
      >
        <v-container>
          <p class="display-0 font-weight-medium">Author Information</p>
          <v-row>
            <v-col>
              <v-text-field 
              prepend-icon="account_circle"
              :rules="[rules.fullnameValidate, rules.required]"
              :validate-on-blur="true"
              :disabled="displayOnly"
              v-model="Author.fullname" label="Full Name" type="text"></v-text-field>
            </v-col>

            <v-col>
              <v-text-field 
              prepend-icon="business"
              :rules="[rules.required]"
              :disabled="displayOnly"
              :validate-on-blur="true"
              v-model="Author.institution" label="Insititution" type="text"></v-text-field>
            </v-col>
          </v-row>

          <v-row>
            <v-col>
              <v-text-field
                prepend-icon="location_on"
                :rules="[ rules.required]"
                :validate-on-blur="true"
                :disabled="displayOnly"
                v-model="Author.region" label="Region" type="text"></v-text-field>
            </v-col>

            <v-col>
              <v-text-field
                prepend-icon="mail"
                :rules="[rules.emailValidate, rules.required]"
                :validate-on-blur="true"
                :disabled="displayOnly"
               v-model="Author.email" label="Email Address" type="text"></v-text-field>
            </v-col>
          </v-row>

          <v-row class="d-flex">
          <v-col class="d-flex justify-end">
            <v-btn
              :disabled="displayOnly"
              id="save"
              color="grey lighten-1"
              class=""
              v-on:click="btnClick()"
            >Save Author
            </v-btn>
          </v-col>
        </v-row>
        </v-container>
      </v-list>
    </v-col>

    
  </v-form>
</template>

<script>

export default {

  props:[
      "displayOnly",
      "AuthorInfo", //the same format as the Author
  ],

  data() {
    return {
    //   menu: false,
      
      Author: {
        fullname: this.AuthorInfo.fullname || "",
        institution: this.AuthorInfo.institution || "",
        region: this.AuthorInfo.region || "",
        email: this.AuthorInfo.email || "",
      },

      rules: {
        required: value => !!value || "This field is required.",

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
      }
    };
  },
  methods: {
    btnClick: function() {
      if(!this.$refs.AuthorForm.validate()){
        return false;
      }
      this.$emit('buttonClick', this.Author)
    }
     
  }
};
</script>