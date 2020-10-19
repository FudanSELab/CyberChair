<template>
  <v-form :rules="rules">
    <v-col cols="10" class="mx-6 py-5">
      <v-list
        color="rgb(232, 232, 232)"
        class="lighten-5 px-10 mx-5 align-center"
        style="background-color: rgb(232, 232, 232)">
        <v-container>
            <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                Meeting name
            </p>
            <v-row>
                <v-col lg="3" md="4" sm="12" class="my-0 py-0">
                    <v-text-field
                    v-model="info.meetingName"
                    type="text"
                    label="Full name"
                    hint=""
                    _hint="Specify the meeting name, such as '12th International meeting on Logic for Programming, Artificial Intelligence, and Reasoning'. Make sure it is properly capitalized, that is do not use 'INTERNATIONAL meeting' or 'international meeting'."
                    :rules="[rules.required]"
                    :validate-on-blur="true"
                    :disabled="display">
                    </v-text-field>
                </v-col>
                
                <v-col lg="3" md="4" sm="12" class="my-0 py-0">
                    <v-text-field
                    v-model="info.acronym"
                    type="text"
                    label="Acronym"
                    _hint="If your converence has no acronym, invent a reasonably short one.
                    If you are not sure what the acronym (abbreviation) is, you can use first letters from every word in the meeting name."
                    :rules="[rules.required]"
                    :validate-on-blur="true"
                    :disabled="display">
                    </v-text-field>
                </v-col>
            </v-row>
        </v-container>   
        
        <v-divider></v-divider>

        <v-container>
            <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                Location
            </p>
            <v-row>
                <v-col lg="3" md="4" sm="12" class="my-0 py-0">
                    <v-text-field
                    v-model="info.city"
                    type="text"
                    label="Venue"
                    :rules="[rules.required]"
                    :validate-on-blur="true"
                    :disabled="display"></v-text-field>
                </v-col>
          
                <v-col lg="3" md="4" sm="12" class="my-0 py-0">
                    <v-text-field
                    v-model="info.venue"
                    type="text"
                    label="City"
                    :rules="[rules.required]"
                    :validate-on-blur="true"
                    :disabled="display"></v-text-field>
                </v-col>
          
                <v-col class="d-flex" lg="3" md="4" sm="12">
                    <v-select
                    v-model="info.region"
                    :items="regions"
                    :rules="[rules.required]"
                    label="Region"
                    dense
                    outlined
                    :disabled="display"></v-select>
                </v-col>
            </v-row>
        </v-container>

        <v-divider></v-divider>

        <v-container v-for="(dateinfo, idx) in dateList"
                     :key="idx"
        >
            <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                {{dateinfo.name }}
            </p>
            <v-row>
                <v-col lg="3" md="4" sm="12" class="my-0 py-0">
                    <v-menu
                        :v-model="info[dateinfo.menuName]"
                        :close-on-content-click="false"
                        :nudge-right="40"
                        transition="scale-transition"
                        offset-y
                        min-width="290px"
                    >
                        <template v-slot:activator="{ on }">
                            <v-text-field
                                v-model="info[dateinfo.dateName]"
                                type="text"
                                :rules="[rules.dateValidate]"
                                label="Pick a date in ten years"
                                :validate-on-blur="true"
                                v-on="on"
                                readonly
                            ></v-text-field>
                        </template>
              
                       <v-date-picker
                            v-model="info[dateinfo.dateName]"
                            :allowed-dates="allowedDates"
                            class="mt-4"
                            @input="info[dateinfo.menuName] = false"
                            :disabled="display"
                        ></v-date-picker>
                    </v-menu>
                </v-col>
            </v-row>
        </v-container>

        <v-container>
            <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                Topics
            </p>

            <v-row>
            <topicOptions
                :displayOnly="display"
                :initTopics="info.topic"
                :availableTopics="areas"
                :availableTopicsRevisable="true"
                @topicChange="getTopics"
            ></topicOptions>
            </v-row>
        </v-container>

        <v-container>
            <p class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1">
                Organizer
            </p>

            <v-row>
                <v-col lg="3" md="4" sm="12" class="my-0 py-0">
                    <v-text-field
                    v-model="info.organizer"
                    type="text"
                    label="Organizer"
                    _hint="Specify the organization or person that organizes the conference. If you have many organizers, only enter the main one."
                    :rules="[rules.required]"
                    :validate-on-blur="true"
                    :disabled="display"></v-text-field>
                </v-col>
                
                <v-col lg="3" md="4" sm="12" class="my-0 py-0">
                    <v-text-field
                    v-model="info.webPage"
                    type="text"
                    label="Web page"
                    _hint="The Web page is mandatory if the organizer has a Web page"
                    :rules="[rules.required]"
                    :validate-on-blur="true"
                    :disabled="display"></v-text-field>
                </v-col>
            </v-row>
        </v-container>

      </v-list>
    </v-col>
  </v-form>
</template>

<script>
import item_list from '../../../components/country'
import topicOptions from '../cyberchairComponents/topicOptions'

export default{
    name: "conferenceInfo",
    props:[
        "displayOnly",
        "conferenceInfo",
    ],
    components:{
        topicOptions,
    },
    data() {
        return {
            info: this.conferenceInfo,
            display : this.displayOnly,
            regions: item_list.country_list,
            areas: item_list.secondary_areas,
            dateList : [
                {
                    "name":"Submission deadline",
                    "menuName":"deadlineMenu",
                    "dateName":"submissionDeadlineDate"
                },
                {
                    "name":"Notification of acceptance",
                    "menuName":"acceptanceMenu",
                    "dateName":"notificationOfAcceptanceDate"
                },
                {
                    "name":"Main conference",
                    "menuName":"mainConferenceMenu",
                    "dateName":"conferenceDate"                 
                },
            ],
            rules: {
                required: value => !!value || 'This field is required.',
                dateValidate: function (val) {
                    let date = new Date();
                    let year = parseInt(val.split('-')[0], 10);
                    let month = parseInt(val.split('-')[1], 10);
                    let day = parseInt(val.split('-')[2], 10);
                    if (year > date.getFullYear() + 10) return 'please choose a date in ten years';
                    if (year < date.getFullYear()) return 'please choose a date after today';
                    if (year === date.getFullYear() && month < date.getMonth()) return 'please choose a date after today';
                    if (year === date.getFullYear() && month === date.getMonth()&& day < date.getDate()) return 'please choose a date after today';
                    return true;
                }
            },
        }
    },
    methods: {
      allowedDates(val) {
        let date = new Date()
        let year = parseInt(val.split('-')[0], 10)
        let month = parseInt(val.split('-')[1], 10)
        let day = parseInt(val.split('-')[2], 10)
        if (year > date.getFullYear() + 10 || year < date.getFullYear()) return false
        if (year === date.getFullYear() + 10 && month > date.getMonth() + 1) return false
        if (year === date.getFullYear() && month < date.getMonth() + 1) return false
        if (year === date.getFullYear() && month === date.getMonth() + 1 && day < date.getDate()) return false
        if (year === date.getFullYear() + 10 && month === date.getMonth() + 1 && day > date.getDate()) return false
        return true
      },
      getTopics(val) {
          this.info.topics = val
      },
    },
}
</script>