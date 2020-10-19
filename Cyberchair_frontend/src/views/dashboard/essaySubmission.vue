<template>
  <v-form
    id="essayApplicationForm"
    ref="essayApplicationForm"
    :model="essayApplierForm"
  >

    <v-col
      cols="10"
      class="mx-6 py-5"
    >

      <v-list
        id="innerEssayForm"
        class="lighten-5 px-5 mx-5 align-center"
      >

      <essayArticle
        :essayTitle="essayApplierForm.essayTitle"
        :essayAbstract="essayApplierForm.essayAbstract"
        :reviseDisable="false"
        v-on:titleChange="essayApplierForm.essayTitle = $event"
        v-on:abstractChange="essayApplierForm.essayAbstract = $event"
        v-if="articleDeatilLoadCompleted || !updateArticle"
      ></essayArticle>

      <p
          class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
        >Topics
        </p>
        <topicOptions
          :initTopics="initArticleTopics"
          :availableTopics="meetingTopics"
          v-on:topicChange="filterTopics($event)"
          v-if=" meetingTopicLoadCompleted&&(articleTopicloadCompleted || !updateArticle) "
        >
        </topicOptions>

        <p
          class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
        >Authors of the essay
        </p>
        <authorRank
          :displayOnly="false"
          :AuthorInfoList="AuthorList"
          v-on:AuthorClick="editAuthorInfo($event)"
          v-if="articleDeatilLoadCompleted || !updateArticle"
        >
        </authorRank>


        <v-row>
          <v-col>
            <v-overflow-btn
              readonly
              label="Add Author"
              v-on:click="newAuthorInfoShowFlag = !newAuthorInfoShowFlag"
              v-model="AuthorList"
              :rules="[rules.requireAuthor]"
            >Add</v-overflow-btn>
          </v-col>
        </v-row>

        <authorInfo
          v-if="newAuthorInfoShowFlag"
          :displayOnly="false"
          :AuthorInfo="newAuthorInfo"
          v-on:buttonClick="addAuthor($event)"
          >

        </authorInfo>


        <p
          v-if="!updateArticle"
          class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
        >Upload essay file (only support PDF files)
        </p>
        <p
          v-if="updateArticle"
          class="display-0 font-weight-medium mt-2 pt-2 mb-1 pb-1"
        >Upload your new essay pdf file(If you want to remain the old version pdf, ignore this step)
        </p>
        <v-file-input
          v-model="essayApplierForm.essayPdf"
          accept=".pdf"
          label="Essay"
          chips
          show-size
          outlined
          prepend-icon=""
          append-icon="attachment"
          :rules="updateArticle?[]:[rules.requireFile]"
          @change="preview"
        ></v-file-input>



        <v-row>
          <v-col>
            <v-btn
              id="submit"
              color="primary"
              class="align-center center"
              v-on:click="submitEssay(essayApplierForm)"
              :disabled="uploadingDisable"
            >SUBMIT
            </v-btn>
          </v-col>
        </v-row>

        <v-row class="d-flex justify-center">
            <canvas ref="myCanvas"></canvas>
        </v-row>

      </v-list>
    </v-col>
  </v-form>
</template>

<script>
// import func from '../../../vue-temp/vue-editor-bridge';
  import pdfJS from 'pdfjs-dist'
  import essayArticle from "./cyberchairComponents/essayArticle"
  import topicOptions from "./cyberchairComponents/topicOptions"
  import authorInfo from "./cyberchairComponents/authorInfo"
  import authorRank from "./cyberchairComponents/authorRank"
  export default {
    components: {
      // pdf
      essayArticle,
      topicOptions,
      authorInfo,
      authorRank
    },
    name: 'DashboardDashboard',
    data() {
      return {


        newAuthorInfoShowFlag:false,
        newAuthorInfo:{
          fullname:"",
          email:"",
          region:"",
          institution:""

        },
        emptyAuthor:{
          fullname:"",
          email:"",
          region:"",
          institution:""
        },
        articleTopicloadCompleted:false,
        meetingTopicLoadCompleted:false,
        articleDeatilLoadCompleted:false,
        updateArticle:false,

        updateArticleId:-1,


        AuthorList: [],
        initArticleTopics:[],
        meetingTopics: [],

        tips_text: '',
        scale: 0.9, // 缩放值

        COMP_essayTitle: "",
        COMP_essayAbstract: "",
        COMP_reviseDisable: false,

        pdfData: '',
        uploadingDisable:false,


        essayApplierForm:{
          essayTitle: '',
          essayAbstract: '',
          essayPdf:undefined,
          meetingName: -1,
          username: '',
          submitTime: new Date().toISOString().substr(0, 10),
        },

        rules:{
          requireFile: function(fileObj){
            console.log(this)
            if(fileObj == undefined){
              return "Please upload your essay pdf file";
            }else{
              // this.preview();
              return true;
            }
          },

          requireAuthor: function(AuthorList){
            if(AuthorList.length == 0) {
              return "You must add at least one author"
            }else return true;
          }
        }
      }
    },

    methods: {
      addAuthor: function(Author){
        // console.log(Author)
        // console.log(Author)
        this.newAuthorInfo = this.emptyAuthor
        this.newAuthorInfoShowFlag = false
        this.AuthorList.push(Author)
        // console.log(this.AuthorList)
      },

      editAuthorInfo: function(Author) {
        this.newAuthorInfo = Author
        this.newAuthorInfoShowFlag = true
        this.AuthorList.splice(this.AuthorList.indexOf(Author), 1)
      },

      submitEssay: function(essayForm) {
        // 
        if(!this.$refs.essayApplicationForm.validate()) return false;
        this.uploadingDisable = true;
        var formData = new FormData();

        formData.append("meetingName", essayForm.meetingName)
        formData.append("username", essayForm.username)
        formData.append('essayTitle', essayForm.essayTitle)
        formData.append('essayAbstract', essayForm.essayAbstract)
        formData.append('submitTime', essayForm.submitTime)
        formData.append('topic', JSON.stringify(this.meetingTopics))
        formData.append('authors', JSON.stringify(this.AuthorList) )

        formData.append('essayPDF', document.querySelector('input[type=file]').files[0])
        // console.log(formData)
        let requestUrl = "api/user/articleSubmission"
        if(this.updateArticle){
          formData.append("articleId", this.updateArticleId)
          requestUrl = "api/user/updateArticle"
        }


        this.$axios.post(requestUrl ,formData)
        .then(resp => {
          if(resp.data.responseCode == 200 && resp.data.responseMessage=="success"){
            this.tips_text = "Success upload your essay submission";
            this.$toast(this.tips_text,{color:'green'})
            var jumpUrl = "/submission/list?authorName="+essayForm.username+"&meetingName="+essayForm.meetingName;
            window.setTimeout(function(){
              this.$router.replace(jumpUrl);
            }.bind(this), 1000);
          }else{
            this.tips_text = "server error " + resp.data.responseBody.reason;
            this.$toast(this.tips_text,{color:'red'})
          }

        })
        .catch(error => {
          this.tips_text = "Communications between server side have errors";
          this.$toast(this.tips_text,{color:'red'})
        })

      },

      loadUrlParams: function(){
        this.essayApplierForm.meetingName = this.$route.query.meetingName;
        this.essayApplierForm.username = this.$route.query.username;
        this.updateArticleId = this.$route.query.articleId;
        if(this.updateArticleId != undefined){
          this.updateArticle = true
        }
        //if not passed, it is an undefined

      },

      loadMeetingTopics: function(meetingName) {
        this.$axios.get(
          'api/meeting/meetingInfo',
          {params:{meetingName: meetingName}}
        )
        .then(resp => {

          if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
            // console.log(eval(resp.data.responseBody.meetingInfo.topic))
            // let topicString = resp.data.responseBody.meetingInfo.topic
            // topicString = topicString.substring(1, topicString.length -1)
            // let topics = topicString.split(", ")
            // // console.log(topics)
            // this.meetingTopics = topics
            let ref = resp.data.responseBody.meetingInfo
            this.meetingTopics = ref.topic
            console.log(ref)
            this.meetingTopicLoadCompleted = true

          }else{
            let tips_text = "internal error occurred, please refresh the page"
            this.$toast(tips_text, {color:"red"})
          }

        })
        .catch(error=> {
          let tips_text = "error occurred when loading meeting topics"
          this.$toast(tips_text,{color:'red'})
        })

      },

      preview(){
        let filePDF = document.querySelector('input[type=file]').files[0];
        let reader = new FileReader();
        reader.readAsDataURL(filePDF);
        
        reader.onload = () =>{
          // console.log(reader.result)
          this.pdfData = atob(reader.result.substring(reader.result.indexOf(',') + 1))
          // console.log(this.pdfData)
          this.previewPDF();
        }


      },
      previewPDF() {
        // 引入pdf.js的字体
        let CMAP_URL = 'https://unpkg.com/pdfjs-dist@2.0.943/cmaps/'
        //读取base64的pdf流文件
        let loadingTask = pdfJS.getDocument({
          data: this.pdfData, // PDF base64编码
          cMapUrl: CMAP_URL,
          cMapPacked: true
        })
        loadingTask.promise.then((pdf) => {
          // this.loadFinished = true
          let numPages = 1
          let pageNumber = 1
          this.getPage(pdf, pageNumber, numPages)
        })
      },
      getPage(pdf, pageNumber, numPages) {
        let _this = this
        pdf.getPage(pageNumber)
          .then((page) => {
            // 获取DOM中为预览PDF准备好的canvasDOM对象
            let canvas = this.$refs.myCanvas
            let viewport = page.getViewport(_this.scale)
            canvas.height = viewport.height
            canvas.width = viewport.width

            let ctx = canvas.getContext('2d')

            let renderContext = {
              canvasContext: ctx,
              viewport: viewport
            }
            page.render(renderContext).then(() => {
              pageNumber += 1
              if (pageNumber <= numPages) {
                _this.getPage(pdf, pageNumber, numPages)
              }
            })
          })
      },
      filterTopics(topicArray){
        console.log(topicArray)
        for(let i = 0; i < topicArray.length; i++){
          if(this.meetingTopics.indexOf(topicArray[i]) < 0){
            topicArray.splice(topicArray.indexOf(topicArray[i]), 1)
            console.log(topicArray)
          }
        }
      },


      loadUploadedArticleDetail() {

        var requestUrl = "api/user/articleDetail"
        this.$axios.get(
            requestUrl,
            {params: {articleId: this.updateArticleId}}
        )
        .then(resp => {

            // console.log(resp)
            if(resp.data.responseCode == 200 && resp.data.responseMessage == "success"){
                var ref = resp.data.responseBody.articleDetail;
                console.log(ref)
                // this.essayApplierForm.essayTitle = "hello world"
                this.essayApplierForm.essayTitle = ref.title;
                this.essayApplierForm.essayAbstract = ref.articleAbstract;
                this.initArticleTopics = ref.topic
                // for(let i = 0; i < ref.authors.length; i++)
                //   this.AuthorList.push(ref.authors[i])
                console.log(ref.authors)
                this.AuthorList = ref.authors
                console.log(this.AuthorList)

                this.essayApplierForm.essayPDFUrl = "api/utils/pdf?pdfUrl="+ref.filePath;

                this.articleDeatilLoadCompleted = true;
                this.articleTopicloadCompleted = true

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

    mounted: function() {
      this.loadUrlParams()
      this.loadMeetingTopics(this.essayApplierForm.meetingName)
      if(this.updateArticleId != undefined){
        this.loadUploadedArticleDetail()
      }
      // this.loadCompleted = true

    },


  }
</script>

<style scoped>

#innerEssayForm{
  background-color: rgb(232, 232, 232) !important;
}

</style>
