package com.jessestolwijk.nfc

import android.app.PendingIntent
import android.content.Intent
import android.graphics.Bitmap
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.NfcAdapter.ACTION_NDEF_DISCOVERED
import android.nfc.NfcAdapter.ACTION_TAG_DISCOVERED
import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import net.sf.scuba.smartcards.CardService
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.jmrtd.BACKey
import org.jmrtd.BACKeySpec
import org.jmrtd.PassportService
import org.jmrtd.lds.CardAccessFile
import org.jmrtd.lds.LDSFileUtil
import org.jmrtd.lds.PACEInfo
import org.jmrtd.lds.SODFile
import org.jmrtd.lds.icao.COMFile
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.DG2File
import java.security.Security

class MainActivity : AppCompatActivity() {

    private lateinit var nfcAdapter: NfcAdapter
    private lateinit var pendingIntent: PendingIntent
    private var mNdefPushMessage: NdefMessage? = null

    init {
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this) ?: run {
            Toast.makeText(this, "No NFC", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        if (!nfcAdapter.isEnabled) {
            Toast.makeText(this, "NFC is not enabled on this device, enable to coninue", Toast.LENGTH_LONG).show()
            return
        }

        pendingIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, this::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
            0
        )

//        mNdefPushMessage = NdefMessage(
//            arrayOf<NdefRecord>(
//                newTextRecord(
//                    "Message from NFC Reader :-)", Locale.ENGLISH, true
//                )
//            )
//        )

    }
//
//    private fun newTextRecord(text: String, locale: Locale, encodeInUtf8: Boolean): NdefRecord {
//        val langBytes = locale.language.toByteArray(Charset.forName("US-ASCII"))
//
//        val utfEncoding = if (encodeInUtf8) Charset.forName("UTF-8") else Charset.forName("UTF-16")
//        val textBytes = text.toByteArray(utfEncoding)
//
//        val utfBit = if (encodeInUtf8) 0 else 1 shl 7
//        val status = (utfBit + langBytes.count()).toChar()
//
//        val data = ByteArray(1 + langBytes.size + textBytes.count())
//        data[0] = status.toByte()
//        System.arraycopy(langBytes, 0, data, 1, langBytes.size)
//        System.arraycopy(textBytes, 0, data, 1 + langBytes.size, textBytes.size)
//
//        return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), data)
//    }

    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null)
//            nfcAdapter.enableForegroundNdefPush(this, mNdefPushMessage)
        }
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this)
//            nfcAdapter.disableForegroundNdefPush(this)
        }
    }


    override fun onNewIntent(intent: Intent) {
        setIntent(intent)

        when (intent.action) {
            ACTION_NDEF_DISCOVERED, ACTION_TAG_DISCOVERED -> {
                val tag = intent.extras.getParcelable<Tag>(NfcAdapter.EXTRA_TAG)!!
                tag.read("NTFR19693", "940922", "250730")

//                intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
//                    val messages: List<NdefMessage> = rawMessages.map { it as NdefMessage }
//                    intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
//                    intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_ID)
//                    // Process the messages array.
//                }
            }
        }
    }

    private fun Tag.read(passportNumber: String, birthDate: String, expirationDate: String) {
        val bacKey = BACKey(passportNumber, birthDate, expirationDate)

        ReadTask(IsoDep.get(this), bacKey).execute()
    }

    private fun ByteArray.asEncodedHexString() = joinToString("") { String.format("%02X", it) }


    private inner class ReadTask(private val isoDep: IsoDep, private val bacKey: BACKeySpec) :
        AsyncTask<Void, Void, Exception>() {

        private var comFile: COMFile? = null
        private var sodFile: SODFile? = null
        private var dg1File: DG1File? = null
        private var dg2File: DG2File? = null
        private var imageBase64: String? = null
        private var bitmap: Bitmap? = null

        override fun doInBackground(vararg params: Void): Exception? {
            val cardService = CardService.getInstance(isoDep)
            cardService.open()
//             int maxTranceiveLength, int maxBlockSize, boolean isSFIEnabled, boolean shouldCheckMAC
            val passportService = PassportService(cardService, 9999, 9999, true, true)
            passportService.open()

            var paceSucceeded = false
            try {
                val cardAccessFile = CardAccessFile(passportService.getInputStream(PassportService.EF_CARD_ACCESS))
                val paceInfos = cardAccessFile.securityInfos
                if (paceInfos != null && paceInfos.isNotEmpty()) {
                    val paceInfo = paceInfos.iterator().next()
                    passportService.doPACE(
                        bacKey,
                        paceInfo.objectIdentifier,
                        PACEInfo.toParameterSpec(paceInfo.)
                    )
                    paceSucceeded = true
                } else {
                    paceSucceeded = true
                }
            } catch (e: Exception) {
            }

            passportService.sendSelectApplet(paceSucceeded)

            if (!paceSucceeded) {
                try {
                    passportService.getInputStream(PassportService.EF_COM).read()
                } catch (e: Exception) {
                    passportService.doBAC(bacKey)
                }

            }

            val comIn = passportService.getInputStream(PassportService.EF_COM)
            comFile = LDSFileUtil.getLDSFile(PassportService.EF_COM, comIn) as COMFile

            val sodIn = passportService.getInputStream(PassportService.EF_SOD)
            sodFile = LDSFileUtil.getLDSFile(PassportService.EF_SOD, sodIn) as SODFile

            val dg1In = passportService.getInputStream(PassportService.EF_DG1)
            dg1File = LDSFileUtil.getLDSFile(PassportService.EF_DG1, dg1In) as DG1File

            val dg2In = passportService.getInputStream(PassportService.EF_DG2)
            dg2File = LDSFileUtil.getLDSFile(PassportService.EF_DG2, dg2In) as DG2File

            val allFaceImageInfos = dg2File!!.faceInfos.map {
                it.faceImageInfos
            }.toTypedArray()


//            if (!allFaceImageInfos.isEmpty()) {
//                val faceImageInfo = allFaceImageInfos.iterator().next()
//
//                val imageLength = faceImageInfo.size
//                val dataInputStream = DataInputStream(faceImageInfo.getImageInputStream())
//                val buffer = ByteArray(imageLength)
//                dataInputStream.readFully(buffer, 0, imageLength)
//                val inputStream = ByteArrayInputStream(buffer, 0, imageLength)
//
//                bitmap = ImageUtil.decodeImage(
//                    this@MainActivity, faceImageInfo.getMimeType(), inputStream
//                )
//                imageBase64 = Base64.encodeToString(buffer, Base64.DEFAULT)
//            }


            return null
        }

        override fun onPostExecute(result: Exception?) {
            print("post")
        }

    }
}
