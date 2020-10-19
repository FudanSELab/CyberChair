<template>
    <v-container>
        <p
          class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
        >Essay Title
        </p>

        <v-row>
          <v-col
            lg="12"
            md="12"
            sm="12"
            class="my-0 py-0"
          >
            <v-text-field
              v-model="title"
              type="text"
              hint=""
              :rules="[rules.essayTitleValidate,rules.required, rules.sqlValidate]"
              :validate-on-blur="true"
              :disabled="disable"
              v-on:change="$emit('titleChange', title)"
            >
            </v-text-field>
          </v-col>
        </v-row>

        <p
          class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
        >Essay Abstract
        </p>

        <v-row>

          <v-col
            lg="12"
            md="12"
            sm="12"
            class="my-0 py-0"
          >
            <v-textarea
              v-model="abstract"
              type="text"
              hint=""
              :rules="[rules.essayAbstractValidate, rules.required, rules.sqlValidate]"
              :disabled="disable"
              :validate-on-blur="true"
              v-on:change="$emit('abstractChange', abstract)"
            >
            </v-textarea>
          </v-col>
        </v-row>
    </v-container>
</template>

<script>
export default {
    props: [
        'essayTitle',
        'essayAbstract',
        'reviseDisable'
    ],

    data () {
        return {
            disable: this.reviseDisable,
            title: this.essayTitle,
            abstract: this.essayAbstract,

            rules: {
                required: value => !!value || 'This field is required.',

                sqlValidate: function(field) {
                    //NOTICE: ' and " keywords are deleted for reality reasons
                    var sqlKeyPattern = /select|update|delete|exec|count|=|;|>|<|%|\\|\'|\"|\-/i;
                    if(sqlKeyPattern.test(field)){
                    return "Please dont include some special characters and SQL keywords"
                    }else {
                    return true;
                    }
                },

                essayTitleValidate: function(essayTitle){
                    
                    if(essayTitle.length <= 50){
                    return true;
                    }else{
                    return "Essay title length should be limited between(1,50), and don't contain special characters";
                    }

                },

                essayAbstractValidate: function(abstract){
                    
                    if(abstract.length <= 800){
                    return true;
                    }else{
                    return "Essay abstract length should be limited between(1,800), and don't contain special characters";
                    }
                }
            },

            
        }

        
    }

}
</script>