<!--
    Pass in the @articleId argument and this component will display
    the content of title abstract and pdf (downloadable)

    Usage
    <essayDetail :articleId="7">
    </essayDetail>
-->
<template>
    <v-list
      id="essayDetail"
      class="blue lighten-5 px-10 mx-5 align-center"
    >

    <essayArticle
      v-if="loadComplete"
      :essayTitle="essayDetail.essayTitle"
      :essayAbstract="essayDetail.essayAbstract"
      :reviseDisable="true"
    >
    </essayArticle>

    <p
        class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
    >Essay PDF overall view
    </p>
    <v-row>
        <v-col cols="6">
            <v-btn
                block
                class="grey lighten-1"
                v-on:click="page = page == 1 ? 1: page-1"
                >Previous Page
            </v-btn>
        </v-col>


      


        <v-col cols="6">
            <v-btn
                block
                class="grey lighten-1"
                v-on:click="page = page == pageCount ? page : page+1"
                >Next Page
            </v-btn>
        </v-col>
    </v-row>

    <pdf
        :src="essayDetail.essayPDFUrl"
        @num-pages="pageCount = $event"
        @page-loaded="currentPage = $event"
        :page="page"
    ></pdf>

    <v-row>
    <v-col cols="12">
            <v-btn
                block
                class="grey lighten-1"
            > <a :href="essayDetail.essayPDFUrl" :download="essayDetail.essayTitle + '.pdf'">Download</a>
            </v-btn>
        </v-col>
    </v-row>
    </v-list>

</template>


<script>
import essayArticle from "./cyberchairComponents/essayArticle"
import pdf from 'vue-pdf'

export default {
    components:{
        essayArticle,
        pdf
    },
    props:[
        "articleId"
    ],

    data() {
        return {
            tips_text: '',
            loadComplete:false,
            essayId:-2,
            essayDetail:{
                essayTitle: "",
                essayAbstract: "",
                essayPDFUrl: ""
            },
            page:1,
            pageCount:-1,
        }


    },

    methods: {
        loadDetailEssay: function(){
            this.essayId = this.articleId;
            var that = this
            var requestUrl = "api/user/articleDetail"
            this.$axios.get(
                requestUrl,
                {params: {
                  articleId: this.essayId,
                }}
            )
            .then(resp => {
                //console.log(resp)
                if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                    var ref = resp.data.responseBody.articleDetail;
                    console.log(ref)
                    that.essayDetail.essayTitle = ref.title;
                    that.essayDetail.essayAbstract = ref.articleAbstract;
                    that.essayDetail.essayPDFUrl = "api/utils/pdf?pdfUrl="+ref.filePath;
                    that.loadComplete = true;

                    // that.essayDetail.essayPDFUrl.replace("\\","/")
                    // console.log('pdfurl')
                    // console.log(that.essayDetail.essayPDFUrl)

                }else{
                    this.tips_text = "error occurred due to " + resp.data.responseBody.reason;
                    this.$toast(this.tips_text,{color:'red'})
                }

            })
            .catch(error => {
                this.tips_text = "error occurred when communicating with the backend";
                this.$toast(this.tips_text,{color:'red'})
          });

        }

    },

    created: function() {
        this.loadDetailEssay()
    },
}
</script>

<style scoped>
#essayDetail{
    background-color: rgb(232, 232, 232) !important;
}
</style>
