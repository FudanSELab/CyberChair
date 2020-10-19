<template>
  <v-container>
    <conferenceInfo
      ref="conferenceInfo"
      :displayOnly = "false"
      :conferenceInfo = "meetingApplierForm"
    ></conferenceInfo>
    <v-col cols="10" class="lighten-5 px-10 mx-5 align-center">
      <v-divider></v-divider>
      <v-form ref="checkbox">
        <v-checkbox
          v-model="checkbox"
          :rules="[v => !!v || 'You must agree to continue!']"
          label='I confirm that I have read and understood Policy On Conferences Using EasyChair and have informed, or will inform, the relevant conference organisers about this policy'
          required
        ></v-checkbox>
      </v-form>
      <v-row>
        <v-col>
          <v-btn
            id="submit"
            color="primary"
            class="align-center"
            v-on:click="applyMeeting(meetingApplierForm)"
            :disabled="uploadDisable"
          >APPLY FOR MEETING
          </v-btn>
        </v-col>
      </v-row>
    </v-col>
  </v-container>
</template>

<script>
import conferenceInfo from '../dashboard/cyberchairComponents/conferenceInfo'
export default {
  name: 'meetingApplier',
  components:{
    conferenceInfo
  },
  data() {
    return {
      checkbox: false,
      uploadDisable:false,
      meetingApplierForm: {
        meetingName: '',
        acronym: '',
        region: '',
        city: '',
        venue: '',
        organizer: '',
        webPage: '',
        deadlineMenu: false,
        acceptanceMenu: false,
        mainConferenceMenu: false,
        submissionDeadlineDate: new Date().toISOString().substr(0, 10),
        notificationOfAcceptanceDate : new Date().toISOString().substr(0, 10),
        conferenceDate : new Date().toISOString().substr(0, 10),
        topic: []
      },
    }
  },
  methods: {
    checkBigDate(date1, date2){
      if(Number(date1[0]) > Number(date2[0])){return false;}
      if(Number(date1[0]) < Number(date2[0])){return true;}
      if(Number(date1[1]) > Number(date2[1])){return false;}
      if(Number(date1[1]) < Number(date2[1])){return true;}
      if(Number(date1[2]) > Number(date2[2])){return false;}
      if(Number(date1[2]) < Number(date2[2])){return true;}
      return true;
    },
    checkTimes(subDate, notiDate, conDate){
      var subdata = subDate.split('-')
      var notidata = notiDate.split('-')
      var condata = conDate.split('-')
      if(!this.checkBigDate(subdata, notidata)){ return false; }
      if(!this.checkBigDate(notidata, condata)){ return false; }
      return true;
    },
    applyMeeting(formName) {
      if(!this.$refs.conferenceInfo.$children[0].validate()){return false;}
      if(!this.$refs.checkbox.validate()){return false;}
      if(!this.checkTimes(this.meetingApplierForm.submissionDeadlineDate, this.meetingApplierForm.notificationOfAcceptanceDate, this.meetingApplierForm.conferenceDate)){
        this.$toast('please enter the time in proper order',{color:'red'});
        return false;
      }
      this.uploadDisable = true;
      this.$axios.post('/api/meeting/application', {
        chairName: localStorage.username,
        meetingName: this.meetingApplierForm.meetingName,
        acronym: this.meetingApplierForm.acronym,
        region: this.meetingApplierForm.region,
        city: this.meetingApplierForm.city,
        venue: this.meetingApplierForm.venue,
        organizer: this.meetingApplierForm.organizer,
        webPage: this.meetingApplierForm.webPage,
        submissionDeadlineDate:this.meetingApplierForm.submissionDeadlineDate,
        notificationOfAcceptanceDate: this.meetingApplierForm.notificationOfAcceptanceDate,
        conferenceDate: this.meetingApplierForm.conferenceDate,
        topic: this.meetingApplierForm.topics,
      })
        .then(resp => {
          if (resp.status === 200) {
            if (resp.data.responseMessage === 'success') {
              this.$toast('successful application',{color:'green'})
              window.setTimeout(function(){
                  this.$router.replace('/dashboard')
              }.bind(this), 2000);
            }
          } else {
              this.$toast('apply error due to ' + resp.data.responseBody.reason,{color:'red'})
          }})
        .catch(error => {
          // delete later
          console.log(error);
          this.$toast("network error",{color:'red'})
        });
    },
  },
}
</script>
