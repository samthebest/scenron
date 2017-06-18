package scenron

object SampleEmails {
  val withForward =
    """
      |Date: Wed, 2 May 2001 01:16:00 -0700 (PDT)
      |From: Vince J Kaminski
      |To: Shirley Crenshaw
      |Cc: Vince J Kaminski, Kristin Gandy
      |Subject: A friend of mine
      |X-SDOC: 1004421
      |X-ZLID: zl-edrm-enron-v2-kaminski-v-21551.eml
      |
      |Shirley,
      |
      |Please, arrange a phone interview with Richard.
      |Stinson, myself, Vasant.
      |
      |Vince
      |
      |
      |---------------------- Forwarded by Vince J Kaminski/HOU/ECT on 05/02/2001
      |08:15 AM ---------------------------
      |From: Kristin Gandy/ENRON@enronXgate on 05/01/2001 05:14 PM
      |To: Vince J Kaminski/HOU/ECT@ECT
      |cc:
      |Subject: A friend of mine
      |
      |Vince,
      |
      |Last week I was contacted by one of my friends who is very interested in blar blar
      |
      |
      |
      |***********
      |EDRM Enron Email Data Set has been produced in EML, PST and NSF format by ZL Technologies, Inc. This Data Set is licensed under a Creative Commons Attribution 3.0 United States License <http://creativecommons.org/licenses/by/3.0/us/> . To provide attribution, please cite to "ZL Technologies, Inc. (http://www.zlti.com)."
      |***********
      |Attachment: Richard Heo Resume.doc type=application/msword
      |
    """.stripMargin.trim()

  val withReply =
    """
      |Date: Thu, 12 Oct 2000 02:24:00 -0700 (PDT)
      |From: Matthew D Williams
      |To: Sophie Kingsley
      |Cc: Karen Tamlyn, Dale Surbey, Steven Leppard, Melanie Doyle, Tani Nath, Vince
      |   J Kaminski, Lucy Page
      |Subject: Re: Matthew Williams
      |X-SDOC: 1032928
      |X-ZLID: zl-edrm-enron-v2-kaminski-v-48945.eml
      |
      |I'm entirely OK with this....
      |
      |Matt
      |
      |
      |
      |Sophie Kingsley   11/10/2000 19:04
      |
      |To: Dale Surbey/LON/ECT@ECT
      |cc: Steven Leppard/LON/ECT@ECT, Melanie Doyle/LON/ECT@ECT, Tani
      |Nath/LON/ECT@ECT, Matthew D Williams/LON/ECT@ECT, Vince J
      |Kaminski/HOU/ECT@ECT, Lucy Page/LON/ECT@ECT
      |
      |Subject: Re: Matthew Williams
      |
      |Let's agree the switch happens November 1st and we will change SAP to reflect
      |
      |Regards
      |
      |SK
      |
      |***********
      |EDRM Enron Email Data Set has been produced in EML, PST and NSF format by ZL Technologies, Inc. This Data Set is licensed under a Creative Commons Attribution 3.0 United States License <http://creativecommons.org/licenses/by/3.0/us/> . To provide attribution, please cite to "ZL Technologies, Inc. (http://www.zlti.com)."
      |***********
      |
    """.stripMargin.trim()

  val withAwkwardReply =
    """
      |Date: Thu, 12 Oct 2000 02:24:00 -0700 (PDT)
      |From: Matthew D Williams
      |To: Sophie Kingsley
      |Cc: Karen Tamlyn, Dale Surbey, Steven Leppard, Melanie Doyle, Tani Nath, Vince
      |   J Kaminski, Lucy Page
      |Subject: Re: Matthew Williams
      |X-SDOC: 1032928
      |X-ZLID: zl-edrm-enron-v2-kaminski-v-48945.eml
      |
      |I'm entirely OK with this....
      |
      |Matt
      |
      |
      |Dale Surbey
      |11/10/2000 18:21
      |To: Steven Leppard/LON/ECT@ECT
      |cc: Melanie Doyle/LON/ECT@ECT, Sophie Kingsley/LON/ECT@ECT, Tani
      |Nath/LON/ECT@ECT, Matthew D Williams/LON/ECT@ECT, Vince J
      |Kaminski/HOU/ECT@ECT
      |
      |Subject: Re: Matthew Williams
      |
      |I agree - sounds like a good idea.
      |
      |- Dale
      |
      |
      |***********
      |EDRM Enron Email Data Set has been produced in EML, PST and NSF format by ZL Technologies, Inc. This Data Set is licensed under a Creative Commons Attribution 3.0 United States License <http://creativecommons.org/licenses/by/3.0/us/> . To provide attribution, please cite to "ZL Technologies, Inc. (http://www.zlti.com)."
      |***********
      |
    """.stripMargin.trim()
}
