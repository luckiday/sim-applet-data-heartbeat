// Applet Display Text de prueba, TELEM

package Heartbeat2;

import javacard.framework.*;
import sim.toolkit.*;
import sim.access.*;

/*
import javacard.framework.*;
import sim.access.*;
//import sim.toolkit.EnvelopeResponseHandler;
import uicc.access.*;
import uicc.toolkit.*;
import sim.toolkit.EnvelopeResponseHandler;
 */

public class Heartbeat2 extends Applet implements ToolkitInterface, ToolkitConstants {
    // SIM
    //public class bip1 extends javacard.framework.Applet implements  ToolkitInterface, uicc.toolkit.ToolkitConstants           // USIM

    public static final byte CMD_QUALIFIER = (byte) 0x80;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // BIP DATA CONSTANTS
    // Constants
    public static final byte DEV_ID_ME = (byte) 0x82;
    public static final byte DCS_8_BIT_DATA = (byte) 0x04;
    public static final byte TAG_SIM_ME_INTERFACE_TRANSPORT_LEVEL = (byte) 0x3C;
    // Display text qualifiers
    public final static byte DTQ_HIGH_PRIORITY = (byte) 0x01;
    public final static byte DTQ_NORMAL_PRIORITY = (byte) 0x00;
    public final static byte DTQ_WAIT_FOR_USER_TO_CLEAR_MESSAGE = (byte) 0x80;
    public final static byte TAG_LOCATION_INFORMATION_93 = (byte) 0x93;
    public static final byte DEV_ID_NETWORK = (byte) 0x83;
    public static final byte PRO_CMD_SEND_SHORT_MESSAGE = (byte) 0x13;
    public static final byte TAG_SMS_TPDU = (byte) 0x0B;
    public final static byte DTQ_CRITICAL = (byte) (DTQ_HIGH_PRIORITY | DTQ_WAIT_FOR_USER_TO_CLEAR_MESSAGE);
    public static final byte TAG_TEXT_STRING_CR = (byte) (TAG_TEXT_STRING | TAG_SET_CR);
    // BIP Constants
    private static final byte TAG_DATA_DESTINATION_ADDRESS = (byte) 0x3E;
    //GPRS = 0x02
    private static final byte BEARER_TYPE_GPRS = (byte) 0x02;
    private static final byte BEARER_PARAMETER_PRECEDENCE_CLASS1 = (byte) 0x01;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static final byte BEARER_PARAMETER_DELAY_CLASS1 = (byte) 0x01;
    private static final byte BEARER_PARAMETER_RELIABILITY_CLASS1 = (byte) 0x01;
    private static final byte BEARER_PARAMETER_PEAK_THROUGHPUT_CLASS1 = (byte) 0x01;
    private static final byte BEARER_PARAMETER_MEAN_THROUGHPUT_CLASS1 = (byte) 0x01;
    // PDP Type = IP
    private static final byte BEARER_PARAMETER_PDP_IP = (byte) 0x02;
    // Type of Address IPV4=21,
    private static final byte TYPE_OF_ADDRESS_IPV4 = (byte) 0x21;
    // SIM/ME interface transport level UDP=01
    private static final byte TRANSPORT_PROTOCOL_TYPE = (byte) 0x01;
    private static final byte[] MENU_ENTRY = {(byte) 'D', (byte) 'e', (byte) 'm', (byte) 'o', (byte) ' ', (byte) 'B', (byte) 'I', (byte) 'P'};
    private static final byte[] HELLO = {(byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o'};
    private static final byte[] HELLO1 = {(byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) '1'};
    private static final byte[] HELLO2 = {(byte) 'H', (byte) 'e', (byte) 'l', (byte) 'l', (byte) 'o', (byte) '2'};
    private static final byte PROFILE_DISPLAY_TEXT = (byte) 16;
    private static final short BUFFER_SIZE = (short) 0x01F4;
    //    private static byte[] myAPN = {(byte) 0x0A, (byte) 'm', (byte) 'y', (byte) 'o', (byte) 'p', (byte) 'e', (byte) 'r', (byte) 'a', (byte) 't', (byte) 'o',
//            (byte) 'r', (byte) 0x09, (byte) 'm', (byte) 'y', (byte) 'c', (byte) 'o', (byte) 'u', (byte) 'n', (byte) 't',
//            (byte) 'r', (byte) 'y'};
    // myoperator.mycountry
    private static byte[] myAPN = {(byte) 0x06, (byte) 's', (byte) 'r', (byte) 's', (byte) 'a', (byte) 'p', (byte) 'n'};
    // Bearer parameters array
    private static byte[] BearerParameters = {(byte) BEARER_TYPE_GPRS, (byte) BEARER_PARAMETER_PRECEDENCE_CLASS1,
            (byte) BEARER_PARAMETER_DELAY_CLASS1, (byte) BEARER_PARAMETER_RELIABILITY_CLASS1,
            (byte) BEARER_PARAMETER_PEAK_THROUGHPUT_CLASS1, (byte) BEARER_PARAMETER_MEAN_THROUGHPUT_CLASS1,
            (byte) BEARER_PARAMETER_PDP_IP};
    // BUFFER FOR HEXTOASCII CONVERTIONS
    private static byte m_BufferInternal[];
    ///// PERSISTENT BUFFER/////
    private static byte[] m_buffer = {(byte) 0x31, (byte) 0x32, (byte) 0x33, (byte) 0x34, (byte) 0x35};
    private static byte[] hexStrBuffer;   // [COMMENT FOR RELEASE]
    private static byte[] portNumber = {(byte) 0x00, (byte) 0x23};                 // Port 13020
    private static byte channelID = 0x00;
    private static byte[] channelData = new byte[BUFFER_SIZE];
    private static short myRspHdlrCapacity;
    private static byte[] IPAddress = {(byte) 96, (byte) 80, (byte) 99, (byte) 17};    // 96.80.99.17 Blade1
    private ToolkitRegistry reg;
    private byte[] menuTitle = {(byte) 'M', (byte) 'e', (byte) 'n', (byte) 'u'};
    private byte[] item1 = {(byte) 'D', (byte) 'i', (byte) 's', (byte) 'p', (byte) 'T', (byte) 'e', (byte) 'x', (byte) 't'};
    private byte[] item2 = {(byte) 'O', (byte) 'p', (byte) 'e', (byte) 'n', (byte) ' ', (byte) 'C', (byte) 'h'};
    private byte[] item3 = {(byte) 'I', (byte) 'T', (byte) 'E', (byte) 'M', (byte) '3'};
    private byte[] item4 = {(byte) 'I', (byte) 'T', (byte) 'E', (byte) 'M', (byte) '4'};
    private Object[] ItemList = {item1, item2, item3, item4};
    private byte result;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    // Constructor of the applet
    public Heartbeat2() {
        // Register to the SIM Toolkit Framework
        // reg = ToolkitRegistry.getEntry();
        // reg = ToolkitRegistrySystem.getEntry();
        // USIM
        reg = ToolkitRegistry.getEntry();
        // SIM
        // Define the menu entry
        reg.initMenuEntry(MENU_ENTRY, (short) 0, (short) MENU_ENTRY.length, PRO_CMD_DISPLAY_TEXT, false, (byte) 0, (short) 0);
    }

    // install method
    public static void install(byte[] buffer, short offset, byte length) throws ISOException {
        Heartbeat2 bip1 = new Heartbeat2();
        bip1.register();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public final static void DisplayText(ProactiveHandler PH, byte[] m_DatatoDisplay) {

        PH.init(PRO_CMD_DISPLAY_TEXT, DTQ_CRITICAL, DEV_ID_DISPLAY);
        PH.appendTLV(TAG_TEXT_STRING_CR, DCS_8_BIT_DATA, m_DatatoDisplay, (short) 0, (short) m_DatatoDisplay.length);
        PH.send();
    }

    /**
     * This function accepts a string of bytes and formats it into a HEX string
     *
     * @param buff_A    buffer to translate to hex string
     * @param index     offset of buff_A where to start converting
     * @param buff_B    buffer to write hex string
     * @param pos       offset of buff_B where to start writing
     * @param lenBuff_A length of buff_A
     */
    //*******************************************************************************
    public static void byteToHexString(byte[] buff_A, short index, byte[] buff_B, short pos, short lenBuff_A) {
        byte hi;
        byte lo;
        byte temp;
        short length = (short) (lenBuff_A + index);

        for (; index < length; index++) {
            hi = (byte) ((byte) (buff_A[index] >> 4) & 0x0F);   //high nibble
            lo = (byte) ((byte) buff_A[index] & 0x0F);          //low nibble

            temp = (byte) 0;
            while (temp < (byte) 2) {
                if ((hi >= 0) && (hi <= (byte) 0x09)) {
                    buff_B[pos++] = (byte) ((byte) '0' + hi);
                } else if ((hi >= (byte) 0x0A) && (hi <= (byte) 0x0F)) {
                    buff_B[pos++] = (byte) ((byte) 0x37 + hi);
                }
                hi = lo;
                temp++;
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private final static void Hex2Acii(byte[] m_Hex, short length2) {
        byte nibL = (byte) 0;
        byte nibL2 = (byte) 0;
        byte nibR = (byte) 0;
        Util.arrayFillNonAtomic(m_BufferInternal, (byte) 0, length2, (byte) 0);
        for (byte i = 0; i < (byte) (length2); i++) {
            nibR = (byte) ((byte) m_Hex[i] & (byte) 0x0F);
            nibL2 = (byte) ((byte) m_Hex[i] & (byte) 0xF0);
            nibL = (byte) ((byte) nibL2 >>> (byte) 4);
            nibL = (byte) ((byte) nibL & (byte) 0x0F);
            if (nibR < (byte) 10) {
                nibR = (byte) (nibR + (byte) 0x30);
            } else {
                nibR = (byte) (nibR + (byte) 0x37);
            }
            if (nibL < (byte) 10) {
                nibL = (byte) (nibL + (byte) 0x30);
            } else {
                nibL = (byte) (nibL + (byte) 0x37);
            }
            m_BufferInternal[(byte) (i * 2)] = (byte) nibL;
            m_BufferInternal[(byte) ((i * 2) + 1)] = (byte) nibR;
        }
        //return m_BufferInternal;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //*** For Testing, comment for Release
    //*******************************************************************************
    //     Method  : byteToHexString
    //     Author  : Sao Paulo Boco
    //    Created  : June 24, 2004
    // Description :

    // process method
    public void process(APDU apdu) throws ISOException {
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // process toolkit method
    //public void processToolkit(byte event) throws ToolkitException
    //public void processToolkit(short event) throws ToolkitException    // USIM
    public void processToolkit(byte event) throws ToolkitException {
        // SIM
        //ProactiveHandler ph = ProactiveHandler.getTheHandler();
        //EnvelopeHandler          envHdlr = EnvelopeHandlerSystem.getTheHandler();          // USIM
        //ProactiveHandler         proHdlr = ProactiveHandlerSystem.getTheHandler();        // USIM

        EnvelopeHandler envHdlr = EnvelopeHandler.getTheHandler();           // SIM
        ProactiveHandler proHdlr = ProactiveHandler.getTheHandler();          // SIM


        //ProactiveResponseHandler rspHdlr = ProactiveResponseHandlerSystem.getTheHandler();  // USIM
        ProactiveResponseHandler rspHdlr = ProactiveResponseHandler.getTheHandler();     // SIM


        // Setup Menu
        proHdlr.init(PRO_CMD_SELECT_ITEM, (byte) 0x00, DEV_ID_ME);
        proHdlr.appendTLV((byte) (TAG_ALPHA_IDENTIFIER | TAG_SET_CR), menuTitle, (short) 0x0000, (short) menuTitle.length);
        for (short i = (short) 0x0000; i < (short) 0x0004; i++) {
            proHdlr.appendTLV((byte) (TAG_ITEM | TAG_SET_CR), (byte) (i + 1), (byte[]) ItemList[i], (short) 0x0000, (short) ((byte[]) ItemList[i]).length);
        }


        if ((result = proHdlr.send()) == RES_CMD_PERF) {
            //rspHdlr = ProactiveResponseHandlerSystem.getTheHandler();
            switch (rspHdlr.getItemIdentifier()) {
                // DISPLAY TEXT MENU
                case 1:
                    proHdlr.init(PRO_CMD_DISPLAY_TEXT, CMD_QUALIFIER, DEV_ID_DISPLAY);
                    proHdlr.appendTLV((byte) (TAG_TEXT_STRING | TAG_SET_CR), DCS_8_BIT_DATA, HELLO, (short) 0x0000, (short) HELLO.length);
                    proHdlr.send();
                    //DisplayText ( proHdlr, HELLO);
                    break;

                // OPEN CHANNEL MENU
                case 2:

                    DisplayText(proHdlr, HELLO);
                    DisplayText(proHdlr, HELLO1);

                    proHdlr.init(PRO_CMD_OPEN_CHANNEL, (byte) 0x00, DEV_ID_ME);
                    proHdlr.appendTLV((byte) (TAG_BEARER_DESCRIPTION | TAG_SET_CR), (byte) BEARER_TYPE_GPRS, BearerParameters, (short) 0x0000, (short) BearerParameters.length);
                    proHdlr.appendTLV((byte) (TAG_BUFFER_SIZE | TAG_SET_CR), (byte) (BUFFER_SIZE >> (byte) 8), (byte) BUFFER_SIZE);
                    proHdlr.appendTLV((byte) (TAG_NETWORK_ACCESS_NAME | TAG_SET_CR), myAPN, (short) 0, (short) myAPN.length);
                    proHdlr.appendTLV((byte) (TAG_SIM_ME_INTERFACE_TRANSPORT_LEVEL | TAG_SET_CR), TRANSPORT_PROTOCOL_TYPE, portNumber, (short) 0, (short) portNumber.length);
                    proHdlr.appendTLV((byte) (TAG_DATA_DESTINATION_ADDRESS | TAG_SET_CR), TYPE_OF_ADDRESS_IPV4, IPAddress, (short) 0, (short) IPAddress.length);

                    result = proHdlr.send();

                    DisplayText(proHdlr, HELLO2);

                    if (result == RES_CMD_PERF) {
                        //rspHdlr = ProactiveResponseHandlerSystem.getTheHandler();
                        channelID = rspHdlr.getChannelIdentifier();
                        myRspHdlrCapacity = rspHdlr.getCapacity();

                        proHdlr.init(PRO_CMD_DISPLAY_TEXT, CMD_QUALIFIER, DEV_ID_DISPLAY);
                        proHdlr.appendTLV((byte) (TAG_TEXT_STRING | TAG_SET_CR), DCS_8_BIT_DATA, HELLO, (short) 0x0000, (short) HELLO.length);
                        proHdlr.send();
                        DisplayText(proHdlr, HELLO1);

                    } else {
                        channelID = 0x00;
                        DisplayText(proHdlr, HELLO2);
                    }

                    break;


                case 3:

                    proHdlr.init(PRO_CMD_DISPLAY_TEXT, CMD_QUALIFIER, DEV_ID_DISPLAY);
                    proHdlr.appendTLV((byte) (TAG_TEXT_STRING | TAG_SET_CR), DCS_8_BIT_DATA, HELLO, (short) 0x0000, (short) HELLO.length);
                    proHdlr.send();


                    //m_buffer[0]=(byte)0x30;
                    //m_buffer[1]=(byte)0x31;

                    proHdlr.init(PRO_CMD_DISPLAY_TEXT, CMD_QUALIFIER, DEV_ID_DISPLAY);
                    proHdlr.appendTLV((byte) (TAG_TEXT_STRING | TAG_SET_CR), DCS_8_BIT_DATA, HELLO2, (short) 0x0000, (short) HELLO2.length);
                    proHdlr.send();

                    proHdlr.init(PRO_CMD_DISPLAY_TEXT, CMD_QUALIFIER, DEV_ID_DISPLAY);
                    proHdlr.appendTLV((byte) (TAG_TEXT_STRING | TAG_SET_CR), DCS_8_BIT_DATA, m_buffer, (short) 0x0000, (short) m_buffer.length);
                    proHdlr.send();


                    byteToHexString(m_buffer, (short) 0, hexStrBuffer, (short) 0, (short) 5);

                    proHdlr.init(PRO_CMD_DISPLAY_TEXT, CMD_QUALIFIER, DEV_ID_DISPLAY);
                    proHdlr.appendTLV((byte) (TAG_TEXT_STRING | TAG_SET_CR), DCS_8_BIT_DATA, hexStrBuffer, (short) 0x0000, (short) hexStrBuffer.length);
                    proHdlr.send();


                    //Hex2Acii( m_buffer, (short)5);
                    //proHdlr.initDisplayText((byte)0x81, (byte)0x04, m_BufferInternal, (short)0, (short)(5*2));
                    //proHdlr.send();


                    //proHdlr.initDisplayText((byte)0x81, (byte)0x04, hexStrBuffer, (short)0, (short)hexStrBuffer.length);
                    //   proHdlr.send();


                    break;


                case 4:
                    proHdlr.init(PRO_CMD_DISPLAY_TEXT, CMD_QUALIFIER, DEV_ID_DISPLAY);
                    proHdlr.appendTLV((byte) (TAG_TEXT_STRING | TAG_SET_CR), DCS_8_BIT_DATA, HELLO2, (short) 0x0000, (short) HELLO2.length);
                    proHdlr.send();
                    break;


            }  // Close switch
        }  // close if
    }
}