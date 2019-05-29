package com.krooms.hostel.rental.property.app.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.krooms.hostel.rental.property.app.R;

/**
 * Created by user on 7/5/2016.
 */
public abstract class TermAndConditionDialog extends DialogFragment {

    private FragmentActivity mFActivity = null;
    private boolean mIsOwner = false;

    public static String htmlTC = "<html>\n" +
            "    <body>\n" +
            "        <div>\n" +
            "            <h3>Terms and conditions</h3>\n" +
            "\n" +
            "<p>These terms and conditions, as may be amended from time to time, apply to all our services directly or indirectly (through distributors) made available online, through any mobile device, by email or by telephone. By accessing, browsing and using our website or any of our applications through whatever platform (hereafter collectively referred to as the \"website\") and/or by completing a reservation, you acknowledge and agree to have read, understood and agreed to the terms and conditions set out below (including the privacy statement).\n" +
            "    These pages, the content and infrastructure of these pages, and the online accommodation reservation service provided on these pages and through the website (the \"service\") are owned, operated and provided by Krooms.in (\"Krooms.in\", \"us\", \"we\" or \"our\") and are provided for your personal, non-commercial use only, subject to the terms and conditions set out below.</p>\n" +
            "\n" +
            "<h3>1. Scope of our Service</h3>\n" +
            "<p>Through the website we (Krooms.in and its affiliate (distribution) partners) provide an online platform through which all types of shortly accommodation (for example, hostels, rooms, pgs, service apartment, studio apartment and food, collectively the \"accommodation(s)\"), can advertise their rooms for reservation, and through which visitors to the website can make such reservations. By making a reservation through Krooms.in, you enter into a direct (legally binding) contractual relationship with the accommodation provider at which you book. From the point at which you make your reservation, we act solely as an intermediary between you and the accommodation, transmitting the details of your reservation to the relevant accommodation provider and sending you a confirmation email for and on behalf of the accommodation provider.</p>\n" +
            "\n" +
            "<p>When rendering our services, the information that we disclose is based on the information provided to us by accommodation providers. As such, the accommodation providers are given access to an extranet through which they are fully responsible for updating all rates, availability and other information which is displayed on our website. Although we will use reasonable skill and care in performing our services we will not verify if, and cannot guarantee that, all information is accurate, complete or correct, nor can we be held responsible for any errors (including manifest and typographical errors), any interruptions (whether due to any (temporary and/or partial) breakdown, repair, upgrade or maintenance of our website or otherwise), inaccurate, misleading or untrue information or non-delivery of information. Each accommodation provider remains responsible at all times for the accuracy, completeness and correctness of the (descriptive) information (including the rates and availability) displayed on our website. Our website does not constitute and should not be regarded as a recommendation or endorsement of the quality, service level, qualification or (star) rating of any accommodation made available.\n" +
            "Our services are made available for personal and non-commercial use only. Therefore, you are not allowed to re-sell, deep-link, use, copy, monitor (e.g. spider, scrape), display, download or reproduce any content or information, software, products or services available on our website for any commercial or competitive activity or purpose.</p>\n" +
            "\n" +
            "<h3>2. Prices:</h3>\n" +
            "<p>The prices on our site are highly competitive. All prices on the Krooms.in website are per room for your entire stay and are displayed including VAT and all other taxes (subject to change of such taxes), unless stated differently on our website or the confirmation email. Applicable taxes may be charged by the accommodation in the event of a no-show or cancellation fee.</p>\n" +
            "\n" +
            "<p>Sometimes cheaper rates are available on our website for a specific stay at an accommodation premises, however, these rates made by accommodations may carry special restrictions and conditions, for example in respect to cancellation and refund. Please check the room and rate details thoroughly for any such conditions prior to making your reservation.</p>\n" +
            "\n" +
            "<p>Obvious errors and mistakes (including misprints) are not binding.\n" +
            "\n" +
            "All special offers and promotions are marked as such.</p>\n" +
            "\n" +
            "<h3>3. Credit card or bank transfer</h3>\n" +
            "<p>If applicable and available, certain accommodation providers offer the opportunity for reservations to be paid (wholly or partly and as required under the payment policy of the accommodation) to the accommodation provider during the reservation process by means of secure online payment (all to the extent offered and supported by your bank). Payment is safely processed from your credit/debit card or bank account to the bank account of the accommodation provider through a third party payment processor.</p>\n" +
            "\n" +
            "<p>For certain (non-refundable) rates or special offers, please note that accommodation providers may require that payment is made upfront by wire transfer (if available) or by credit card, and therefore your credit card may be pre-authorised or charged (sometimes without any option for refund) upon making the reservation. Please check the room details thoroughly for any such conditions prior to making your reservation.</p>\n" +
            "\n" +
            "<h3>4. Cancellation and no-show</h3>\n" +
            "<p>By making a reservation with an accommodation provider, you accept and agree to the relevant cancellation and no-show policy of that accommodation provider, and to any additional (delivery) terms and conditions of the accommodation provider that may apply to your reservation or during your stay, including for services rendered and/or products offered by the accommodation provider (the delivery terms and conditions of an accommodation provider can be obtained with the relevant accommodation provider). The general cancellation and no-show policy of each accommodation provider is made available on our website on the accommodation information pages, during the reservation procedure and in the confirmation email. Please note that certain rates or special offers are not eligible for cancellation or change. Applicable city/tourist tax may still be charged by the accommodation in the event of a no-show or charged cancellation. Please check the room details thoroughly for any such conditions prior to making your reservation. Please note that a reservation which requires down payment or (wholly or partly) prepayment may be cancelled (without a prior notice of default or warning) insofar the relevant (remaining) amount(s) cannot be collected in full on the relevant payment date in accordance with the relevant payment policy of the accommodation and the reservation. Late payment, wrong bank, debit or credit card details, invalid credit/debit cards or insufficient funds are for your own risk and account and you shall not be entitled to any refund of any (non-refundable) prepaid amount unless the accommodation agrees or allows otherwise under its (pre)payment and cancellation policy.\n" +
            "If you wish to review, adjust or cancel your reservation, please revert to the confirmation email and follow the instructions therein. Please note that you may be charged for your cancellation in accordance with the accommodation provider's cancellation, (pre)payment and no-show policy or not be entitled to any repayment of any (pre)paid amount. We recommend that you read the cancellation, (pre)payment and no-show policy of the accommodation provider carefully prior to making your reservation and remember to make further payments on time as may be required for the relevant reservation.</p>\n" +
            "\n" +
            "<p>If you have a late or delayed arrival on the check-in date or only arrive the next day, make sure to (timely/promptly) communicate this with the accommodation so they know when to expect you to avoid cancellation of your room or charge of the no-show fee. Our customer service department can help you if needed with informing the property. Krooms.in does not accept any liability or responsibility for the consequences of your delayed arrival or any cancellation or charged no-show fee by the accommodation.\n" +
            "Cancellaion is refundable only if the tenant find discrepancy in the property as claimed trough virtually representation. The refund must though be initiated within a week from booking. After 7 days Krooms will not be responsible for refund. The perties must settle their disputes mutually.</p>\n" +
            "\n" +
            "<p>If the booking is cancelled or changed by tenant, the owner will be notified and this confirmation email & Krooms Booking ID will be null and void.\n" +
            "For detailed cancellation policy Please go through our terms and conditions.\n" +
            "Booking can be cancelled only through customer support call centre.</p>\n" +
            "\n" +
            "\n" +
            "\n" +
            "<h3>5. (Further) correspondence and communication</h3>\n" +
            "<p>By completing a booking, you agree to receive (i) an email which we may send you shortly prior to your arrival date, giving you information on your destination and providing you with certain information and offers (including third party offers to the extent that you have actively opted in for this information) relevant to your reservation and destination, and (ii) an email which we may send to you promptly after your stay inviting you to complete our guest review form. Please see our privacy and cookies policy for more information about how we may contact you.\n" +
            "Krooms.in disclaims any liability or responsibility for any communication with the accommodation on or through its platform. You cannot derive any rights from any request to, or communication with the accommodation or (any form of) acknowledgement of receipt of any communication or request. Krooms.in cannot guarantee that any request or communication will be (duly and timely) received/read by, complied with, executed or accepted by the accommodation.</p>\n" +
            "\n" +
            "<p>In order to duly complete and secure your reservation, you need to use your correct email address. We are not responsible or liable for (and have no obligation to verify) any wrong or misspelled email address or inaccurate or wrong (mobile) phone number or credit card number.</p>\n" +
            "\n" +
            "<h3>6. Ranking, preferred programme, stars and guest reviews</h3>\n" +
            "<p>The default setting of the ranking of accommodation on our website is 'Recommended' (or any similar wording) (the \"Default Ranking\"). For your convenience we also offer other ways to rank accommodation. Please note that the Default Ranking is created through a fully automatic ranking system (algorithm) and based on multiple criteria.</p>\n" +
            "\n" +
            "<p>The completed guest review may be (a) uploaded onto the relevant accommodation property's information page on our website for the sole purpose of informing (future) customers of your opinion of the service (level) and quality of the accommodation, and (b) (wholly or partly) used and placed by Krooms.in at its sole discretion (e.g. for marketing, promotion or improvement of our service) on our website or such social media platforms, newsletters, special promotions, apps or other channels owned, hosted, used or controlled by Krooms.in and our business partners. We reserve the right to adjust, refuse or remove reviews at our sole discretion. The guest review form should be regarded as a survey and does not include any (further commercial) offers, invitations or incentives whatsoever.</p>\n" +
            "\n" +
            "<h3>7. Disclaimer</h3>\n" +
            "<p>Subject to the limitations set out in these terms and conditions and to the extent permitted by law, we shall only be liable for direct damages actually suffered, paid or incurred by you due to an attributable shortcoming of our obligations in respect to our services, up to an aggregate amount of the aggregate cost of your reservation as set out in the confirmation email (whether for one event or series of connected events).\n" +
            "However and to the extent permitted by law, neither we nor any of our officers, directors, employees, representatives, subsidiaries, affiliated companies, distributors, affiliate (distribution) partners, licensees, agents or others involved in creating, sponsoring, promoting, or otherwise making available the site and its contents shall be liable for (i) any punitive, special, indirect or consequential loss or damages, any loss of production, loss of profit, loss of revenue, loss of contract, loss of or damage to goodwill or reputation, loss of claim, (ii) any inaccuracy relating to the (descriptive) information (including rates, availability and ratings) of the accommodation as made available on our website, (iii) the services rendered or the products offered by the accommodation provider or other business partners, (iv) any (direct, indirect, consequential or punitive) damages, losses or costs suffered, incurred or paid by you, pursuant to, arising out of or in connection with the use, inability to use or delay of our website, or (v) any (personal) injury, death, property damage, or other (direct, indirect, special, consequential or punitive) damages, losses or costs suffered, incurred or paid by you, whether due to (legal) acts, errors, breaches, (gross) negligence, wilful misconduct, omissions, non-performance, misrepresentations, tort or strict liability by or (wholly or partly) attributable to the accommodation or any of our other business partners (including any of their employees, directors, officers, agents, representatives or affiliated companies) whose products or service are (directly or indirectly) made available, offered or promoted on or through the website, including any (partial) cancellation, overbooking, strike, force majeure or any other event beyond our control.</p>\n" +
            "\n" +
            "<p>Whether the accommodation you stay at charges you (or has charged you) for your room, or we are facilitating the payment of the room price, you agree and acknowledge that the accommodation is at all times responsible for the collection, withholding, remittance and payment of the applicable taxes due on the total amount of the room price to the relevant tax authorities. Krooms.in is not liable or responsible for the remittance, collection, withholding or payment of the relevant taxes due on the room price to the relevant tax authorities.</p>\n" +
            "\n" +
            "<p>By uploading photos/images onto our system (for instance in addition to a review) you certify, warrant and agree that you own the copyright to the photos/images and that you agree that Krooms.in may use the uploaded photos/images on its (mobile) website and app, and in (online/offline) promotional materials and publications and as Krooms.in at its discretion sees fit. You are granting Krooms.in a non-exclusive, worldwide, irrevocable, unconditional, perpetual right and license to use, reproduce, display, have reproduced, distribute, sublicense, communicate and make available the photos/images as Krooms.in at its discretion sees fit. By uploading these photos/images the person uploading the picture(s) accepts full legal and moral responsibility of any and all legal claims that are made by any third parties (including, but not limited to, accommodation owners) due to Krooms.in publishing and using these photos/images. Krooms.in does not own or endorse the photos/images that are uploaded. The truthfulness, validity and right to use of all photos/images is assumed by the person who uploaded the photo, and is not the responsibility of Krooms.in. Krooms.in disclaims all responsibility and liability for the pictures posted. The person who uploaded the photo warrants that the photos/images shall not contain any viruses, Trojan horses or infected files and shall not contain any pornographic, illegal, obscene, insulting, objectionable or inappropriate material and does not infringe any third party (intellectual property right, copyright or privacy) rights. Any photo/image that does not meet the aforesaid criteria will not be posted and/or can be removed/deleted by Krooms.in at any time and without prior notice.</p>\n" +
            "\n" +
            "<h3>8. Intellectual property rights</h3>\n" +
            "<p>Unless stated otherwise, the software required for our services or available at or used by our website and the intellectual property rights (including the copyrights) of the contents and information of and material on our website are owned by Krooms.in, its suppliers or providers.</p>\n" +
            "\n" +
            "<p>Krooms.in exclusively retains ownership of all rights, title and interest in and to (all intellectual property rights of) (the look and feel (including infrastructure) of) the website on which the service is made available (including the guest reviews and translated content) and you are not entitled to copy, scrape, (hyper-/deep)link to, publish, promote, market, integrate, utilize, combine or otherwise use the content (including any translations thereof and the guest reviews) or our brand without our express written permission. To the extent that you would (wholly or partly) use or combine our (translated) content (including guest reviews) or would otherwise own any intellectual property rights in the website or any (translated) content or guest reviews, you hereby assign, transfer and set over all such intellectual property rights to Krooms.in. Any unlawful use or any of the aforementioned actions or behaviour will constitute a material infringement of our intellectual property rights (including copyright and database right).</p>\n" +
            "\n" +
            "<h3>9. Miscellaneous</h3>\n" +
            "<p>To the extent permitted by law, these terms and conditions and the provision of our services shall be governed by and construed in accordance with Indian law and any dispute arising out of these general terms and conditions and our services shall exclusively be submitted to the competent courts in Indore, Madhya Pradesh.\n" +
            "The original English version of these terms and conditions may have been translated into other languages. The translated version is a courtesy and office translation only and you cannot derive any rights from the translated version. In the event of a dispute about the contents or interpretation of these terms and conditions or inconsistency or discrepancy between the English version and any other language version of these terms and conditions, the English language version to the extent permitted by law shall apply, prevail and be conclusive. The English version is available on our website (by selecting the English language) or shall be sent to you upon your written request.</p>\n" +
            "\n" +
            "<p>If any provision of these terms and conditions is or becomes invalid, unenforceable or non-binding, you shall remain bound by all other provisions hereof. In such event, such invalid provision shall nonetheless be enforced to the fullest extent permitted by applicable law, and you will at least agree to accept a similar effect as the invalid, unenforceable or non-binding provision, given the contents and purpose of these terms and conditions.</p>\n" +
            "     \n" +
            "        </div>\n" +
            "    </body>\n" +
            "</html>";

    public static String ownerHtmlTC = "<html>    \n" +
            "    <body>\n" +
            "        <div style=\"overflow-y: scroll;\">\n" +
            "            <h3>  NOW THEREFORE THE PARTIES HERETO AGREE TO THIS AGREEMENT TERMS AS FOLLOWS</h3>\n" +
            "            <ul>\n" +
            "                <li><p>The owner will provide inventory to Krooms on a direct connection (API linking) or Extranet or on request or on allocation basis as the case may be. This will enable Krooms to host the availability of the room’s inventory on Krooms Application. Bookings of the rooms will be purely at the choice of the tenants.</p></li>\n" +
            "                <li><p>The inventory rates, the blocking and releasing time lines, other commercial details will be as mutually agreed by the parties on case to case basis by email or in writing.</p></li>\n" +
            "                <li><p>The owner shall pay to KROOMS a commission at the rate mentioned in the form submitted for the bookings done by KROOMS. The parties will settle the payment obligations including the commissions within 7 days of booking.</p></li>\n" +
            "                <li><p>Cancellation charges and the rules will be as per the property specific cancellation policy given to KROOMS by email or in writing. No cancellation retention to be paid to the owner in case of force majeure events.</p></li>\n" +
            "<li><p>The owner will honour all KROOMS bookings without fail. In the extreme cases where accommodating the customer in the said property is not possible for any reason what so ever, then the Owner will provide the customer with an alternate similar accommodation in same category property in the same or nearest locality at no extra cost, including transfers.</p></li>\n" +
            "<li><p>The validity of the contract would hold perpetuity unless either party gives one month termination notice to the other party in writing/ email. However the accrued obligations of the parties prior to the cancellation will continue to be fulfilled post cancellation.</p></li>\n" +
            "<li><p>This Agreement is governed by the laws of INDIA and the jurisdiction is exclusively of Indore courts. The parties agree that, before initiating any litigation, they will attempt to resolve their dispute through arbitration. The arbitration panel comprises of 3 arbitrators, of whom two are appointed by each of the parties and the third person being appointed by the former two arbitrators.</p></li>\n" +
            "<li><p>The bookings of KROOMS will as per the user agreement and the parties agree to deliver their obligations accordingly. The User Agreement is for Owner's reference. The User Agreement is subject to changes and the Owner must visit at http://www.Krooms.in/legal/user_agreement.html for the updated version of the User Agreement.</p></li>\n" +
            "<li><p>The information about the facilities available at the property, the vicinity details, star rating, etc shall be as mentioned as informed by email to KROOMS from time to time.\n" +
            "For accommodation bookings, this Agreement constitutes the complete and exclusive statement of the agreement between the parties, and supersedes all other communications.</li>\n" +
            "\n" +
            "\n" +
            "<h3>APPLICABILITY OF THE AGREEMENT:</h3>\n" +
            "\n" +
            "<p>This agreement (\"user agreement\") incorporates the terms and conditions for Kyra Business Consultancy Pvt. Ltd and its affiliate Companies (\"KROOMS\") to provide services to the person (s) (\"the User\") intending to purchase or inquiring for any products and/ or services of KROOMS by using KROOMS's websites or using any other customer interface channels of KROOMS which includes its sales persons, offices, call centres, advertisements, information campaigns etc.</p>\n" +
            "<p>Both User and KROOMS are individually referred as 'party' to the agreement and collective referred to as 'parties'.</p>\n" +
            "\n" +
            "<h3>USER'S RESPONSIBILITY OF COGNIZANCE OF THIS AGREEMENT</h3>\n" +
            "\n" +
            "<p>The Users availing services from KROOMS shall be deemed to have read, understood and expressly accepted the terms and conditions of this agreement, which shall govern the desired transaction or provision of such services by KROOMS for all purposes, and shall be binding on the User. All rights and liabilities of the User and/or KROOMS with respect to any services to be provided by KROOMS shall be restricted to the scope of this agreement.</p>\n" +
            "\n" +
            "<p>KROOMS reserves the right, in its sole discretion, to terminate the access to any or all KROOMS websites or its other sales channels and the related services or any portion thereof at any time, without notice, for general maintenance or any reason what so ever.</p>\n" +
            "\n" +
            "<p>KROOMS's Services are offered to the User conditioned on acceptance without modification of all the terms, conditions and notices contained in this Agreement and the TOS, as may be applicable from time to time. For the removal of doubts, it is clarified that availing of the Services by the User constitutes an acknowledgement and acceptance by the User of this Agreement and the TOS. If the User does not agree with any part of such terms, conditions and notices, the User must not avail KROOMS's Services.</p>\n" +
            "\n" +
            "<p>In the event that any of the terms, conditions, and notices contained herein conflict with the Additional Terms or other terms and guidelines contained within any other KROOMS document, then these terms shall control.</p>\n" +
            " \n" +
            "\n" +
            "<h3>THIRD PARTY ACCOUNT INFORMATION</h3>\n" +
            "\n" +
            "<p>By using the Account Access service in KROOMS's websites, the User authorizes KROOMS and its agents to access third party sites, including that of Banks and other payment gateways, designated by them or on their behalf for retrieving requested information</p>\n" +
            "\n" +
            "<p>While registering, the User will choose a password and is responsible for maintaining the confidentiality of the password and the account.</p>\n" +
            "\n" +
            "<p>The User is fully responsible for all activities that occur while using their password or account. It is the duty of the User to notify KROOMS immediately in writing of any unauthorized use of their password or account or any other breach of security. KROOMS will not be liable for any loss that may be incurred by the User as a result of unauthorized use of his password or account, either with or without his knowledge. The User shall not use anyone else's password at any time</p>\n" +
            "\n" +
            "<p>KROOMS reserves the right to charge listing fees for certain listings, as well as transaction fees based on certain completed transactions using the services. KROOMS further reserves the right to alter any and all fees from time to time, without notice.</p>\n" +
            "\n" +
            "<p>The User shall be completely responsible for all charges, fees, duties, taxes, and assessments arising out of the use of the services.</p>\n" +
            "\n" +
            "<p>In case, there is a short charging by KROOMS for listing, services or transaction fee or any other fee or service because of any technical or other reason, it reserves the right to deduct/charge/claim the balance subsequent to the transaction at its own discretion.</p>\n" +
            "\n" +
            "<p>Any increase in the price charged by Company on account of change in rate of taxes or imposition of new taxes by Government shall have to be borne by customer.</p>\n" +
            "\n" +
            "In the rare possibilities of the reservation not getting confirmed for any reason whatsoever, we will process the refund and intimate you of the same. Krooms is not under any obligation to make another booking in lieu of or to compensate/ replace the unconfirmed one. All subsequent further bookings will be treated as new transactions with no reference to the earlier unconfirmed reservation.\n" +
            "\n" +
            "The User shall request Krooms for any refunds against the unutilized or 'no show' for any reasons within 30 days from the date of hire. Any applicable refunds would accordingly be processed as per the defined policies of Krooms. No refund would be payable for any requests made after the expiry of 30 days as above and all unclaimed amounts for such unutilized or no show booking shall be deemed to have been forfeited.\n" +
            " \n" +
            "\n" +
            "<h3>CONFIDENTIALITY</h3>\n" +
            "\n" +
            "<p>Any information which is specifically mentioned by KROOMS as Confidential shall be maintained confidentially by the user and shall not be disclosed unless as required by law or to serve the purpose of this agreement and the obligations of both the parties therein.</p>\n" +
            "\n" +
            "\n" +
            "<h3>SAGE OF THE MOBILE NUMBER OF THE USER BY KROOMS</h3>\n" +
            "\n" +
            "<p>KROOMS may send booking confirmation, itinerary information, cancellation, payment confirmation, refund status, schedule change or any such other information relevant for the transaction, via SMS or by voice call on the contact number given by the User at the time of booking; KROOMS may also contact the User by voice call, SMS or email in case the User couldn’t or hasn’t concluded the booking, for any reason what so ever, to know the preference of the User for concluding the booking and also to help the User for the same. The User hereby unconditionally consents that such communications via SMS and/ or voice call by KROOMS is (a) upon the request and authorization of the User, (b) ‘transactional’ and not an ‘unsolicited commercial communication’ as per the guidelines of Telecom Regulation Authority of India (TRAI) and (c) in compliance with the relevant guidelines of TRAI or such other authority in India and abroad. The User will indemnify KROOMS against all types of losses and damages incurred by KROOMS due to any action taken by TRAI, Access Providers (as per TRAI regulations) or any other authority due to any erroneous compliant raised by the User on KROOMS with respect to the intimations mentioned above or due to a wrong number or email id being provided by the User for any reason whatsoever.</p>\n" +
            "\n" +
            "\n" +
            "<h3>ONUS OF THE USER</h3>\n" +
            "\n" +
            "<p>KROOMS is responsible only for the transactions that are done by the User through KROOMS. KROOMS will not be responsible for screening, censoring or otherwise controlling transactions, including whether the transaction is legal and valid as per the laws of the land of the User.\n" +
            "The User warrants that they will abide by all such additional procedures and guidelines, as modified from time to time, in connection with the use of the services. The User further warrants that they will comply with all applicable laws and regulations regarding use of the services with respect to the jurisdiction concerned for each transaction.</p>\n" +
            "\n" +
            "<p>The User represent and confirm that the User is of legal age to enter into a binding contract and is not a person barred from availing the Services under the laws of India or other applicable law.</p>\n" +
            "\n" +
            "\n" +
            "<h3>ADVERTISERS ON KROOMS OR LINKED WEBSITES</h3>\n" +
            "\n" +
            "<p>KROOMS is not responsible for any errors, omissions or representations on any of its pages or on any links or on any of the linked website pages. KROOMS does not endorse any advertiser on its web pages in any manner. The Users are requested to verify the accuracy of all information on their own before undertaking any reliance on such information.</p>\n" +
            "\n" +
            "<p>The linked sites are not under the control of KROOMS and KROOMS is not responsible for the contents of any linked site or any link contained in a linked site, or any changes or updates to such sites. KROOMS is providing these links to the Users only as a convenience and the inclusion of any link does not imply endorsement of the site by KROOMS.</p>\n" +
            "\n" +
            " \n" +
            "<h3>FORCE MAJURE CIRCUMSTANCES</h3>\n" +
            "\n" +
            "<p>The User agrees that in situations due to any technical or other failure in KROOMS, services committed earlier may not be provided or may involve substantial modification. In such cases, KROOMS shall refund the entire amount received from the customer for availing such services minus the applicable cancellation, refund or other charges, which shall completely discharge any and all liabilities of KROOMS against such non-provision of services or deficiencies. Additional liabilities, if any, shall be borne by the User.</p>\n" +
            "\n" +
            "<p>KROOMS shall not be liable for delays or inabilities in performance or non-performance in whole or in part of its obligations due to any causes that are not due to its acts or omissions and are beyond its reasonable control, such as acts of God, fire, strikes, embargo, acts of government, acts of terrorism or other similar causes, problems at owners end. In such event, the user affected will be promptly given notice as the situation permits.</p>\n" +
            "\n" +
            "<p>Without prejudice to whatever is stated above, the maximum liability on part of KROOMS arising under any circumstances, in respect of any services offered on the site, shall be limited to the refund of total amount received from the customer for availing the services less any cancellation, refund or others charges, as may be applicable. In no case the liability shall include any loss, damage or additional expense whatsoever beyond the amount charged by KROOMS for its services.</p>\n" +
            "\n" +
            "<p>In no event shall KROOMS and/or its suppliers be liable for any direct, indirect, punitive, incidental, special, consequential damages or any damages whatsoever including, without limitation, damages for loss of use, data or profits, arising out of or in any way connected with the use or performance of the KROOMS website(s) or any other channel . Neither shall KROOMS be responsible for the delay or inability to use the KROOMS websites or related services, the provision of or failure to provide services, or for any information, software, products, services and related graphics obtained through the KROOMS website(s), or otherwise arising out of the use of the KROOMS website(s), whether based on contract, tort, negligence, strict liability or otherwise.</p>\n" +
            "\n" +
            "<p>KROOMS is not responsible for any errors, omissions or representations on any of its pages or on any links or on any of the linked website pages.</p>\n" +
            "\n" +
            "\n" +
            "<h3>SAFETY OF DATA DOWNLOADED</h3>\n" +
            "\n" +
            "<p>The User understands and agrees that any material and/or data downloaded or otherwise obtained through the use of the Service is done entirely at their own discretion and risk and they will be solely responsible for any damage to their computer systems or loss of data that results from the download of such material and/or data.</p>\n" +
            "\n" +
            "<p>Nevertheless, KROOMS will always make its best endeavours to ensure that the content on its websites or other information channels are free of any virus or such other malwares.</p>\n" +
            "\n" +
            "\n" +
            "<h3>FEEDBACK FROM CUSTOMER AND SOLICITATION:</h3>\n" +
            "\n" +
            "<p>The User is aware that Krooms provides booking services and would like to learn about them, to enhance his /her experience. The User hereby specifically authorizes Krooms to contact the User with offers on various services offered by it through direct mailers, e-mailers, telephone calls, short messaging services (SMS) or any other medium, from time to time. In case that the customer chooses not to be contacted, he/she shall write to Krooms for specific exclusion at service@Krooms.com or provide his/her preferences to the respective service provider. The customers are advised to read and understand the privacy policy of KROOMS on its website in accordance of which KROOMS contacts, solicits the user or shares the user's information.</p>\n" +
            "\n" +
            "\n" +
            "<h3>PROPRIETARY RIGHTS</h3>\n" +
            "\n" +
            "<p>KROOMS may provide the User with contents such as sound, photographs, graphics, video or other material contained in sponsor advertisements or information. This material may be protected by copyrights, trademarks, or other intellectual property rights and laws.</p>\n" +
            "\n" +
            "<p>The User may use this material only as expressly authorized by KROOMS and shall not copy, transmit or create derivative works of such material without express authorization.</p>\n" +
            "\n" +
            "<p>The User acknowledges and agrees that he/she shall not upload post, reproduce, or distribute any content on or through the Services that is protected by copyright or other proprietary right of a third party, without obtaining the written permission of the owner of such right.</p>\n" +
            "\n" +
            "<p>Any copyrighted or other proprietary content distributed with the consent of the owner must contain the appropriate copyright or other proprietary rights notice. The unauthorized submission or distribution of copyrighted or other proprietary content is illegal and could subject the User to personal liability or criminal prosecution.</p>\n" +
            "\n" +
            "\n" +
            "<h3>PERSONAL AND NON-COMMERCIAL USE LIMITATION</h3>\n" +
            "\n" +
            "<p>Unless otherwise specified, the KROOMS services are for the User's personal and non - commercial use. The User may not modify copy, distribute, transmit, display, perform, reproduce, publish, license, create derivative works from, transfer, or sell any information, software, products or services obtained from the KROOMS website(s) without the express written approval from KROOMS.</p>\n" +
            "\n" +
            "\n" +
            "<h3>INDEMNIFICATION</h3>\n" +
            "\n" +
            "<p>The User agrees to indemnify, defend and hold harmless KROOMS and/or its affiliates, their websites and their respective lawful successors and assigns from and against any and all losses, liabilities, claims, damages, costs and expenses (including reasonable legal fees and disbursements in connection therewith and interest chargeable thereon) asserted against or incurred by KROOMS and/or its affiliates, partner websites and their respective lawful successors and assigns that arise out of, result from, or may be payable by virtue of, any breach or non-performance of any representation, warranty, covenant or agreement made or obligation to be performed by the User pursuant to this agreement.</p>\n" +
            "\n" +
            "<p>The user shall be solely and exclusively liable for any breach of any country specific rules and regulations or general code of conduct and KROOMS cannot be held responsible for the same.</p>\n" +
            "\n" +
            " \n" +
            "<h3>RIGHT TO REFUSE</h3>\n" +
            "\n" +
            "<p>KROOMS at its sole discretion reserves the right to not to accept any customer order without assigning any reason thereof. Any contract to provide any service by KROOMS is not complete until full money towards the service is received from the customer and accepted by KROOMS.</p>\n" +
            "\n" +
            "<p>Without prejudice to the other remedies available to KROOMS under this agreement, the TOS or under applicable law, KROOMS may limit the user's activity, or end the user's listing, warn other users of the user's actions, immediately temporarily/indefinitely suspend or terminate the user's registration, and/or refuse to provide the user with access to the website if:<br>\n" +
            "<ul><li><p>The user is in breach of this agreement, the TOS and/or the documents it incorporates by reference;</p></li>\n" +
            "<li><p>KROOMS is unable to verify or authenticate any information provided by the user; or</p></li>\n" +
            "<li><p>KROOMS believes that the user's actions may infringe on any third party rights or breach any applicable law or otherwise result in any liability for the user, other users of the website and/or KROOMS.</p></li></ul></p>\n" +
            "\n" +
            "<p>KROOMS may at any time in its sole discretion reinstate suspended users. Once the user have been indefinitely suspended the user shall not register or attempt to register with KROOMS or use the website in any manner whatsoever until such time that the user is reinstated by KROOMS.</p>\n" +
            "\n" +
            "<p>Notwithstanding the foregoing, if the user breaches this agreement, the TOS or the documents it incorporates by reference, KROOMS reserves the right to recover any amounts due and owing by the user to KROOMS and/or the service provider and to take strict legal action as KROOMS deems necessary.</p>\n" +
            "\n" +
            "\n" +
            "<h3>RIGHT TO CANCELLATION BY KROOMS IN CASE OF INVALID INFROMATION FROM USER</h3>\n" +
            "\n" +
            "<p>The User expressly undertakes to provide to KROOMS only correct and valid information while requesting for any services under this agreement, and not to make any misrepresentation of facts at all. Any default on part of the User would vitiate this agreement and shall disentitle the User from availing the services from KROOMS.</p>\n" +
            "\n" +
            "<p>In case KROOMS discovers or has reasons to believe at any time during or after receiving a request for services from the User that the request for services is either unauthorized or the information provided by the User or any of them is not correct or that any fact has been misrepresented by him, KROOMS in its sole discretion shall have the unrestricted right to take any steps against the User(s), including cancellation of the bookings, etc. without any prior intimation to the User. In such an event, KROOMS shall not be responsible or liable for any loss or damage that may be caused to the User or any of them as a consequence of such cancellation of booking or services.</p>\n" +
            "\n" +
            "<p>The User unequivocally indemnifies KROOMS of any such claim or liability and shall not hold KROOMS responsible for any loss or damage arising out of measures taken by KROOMS for safeguarding its own interest and that of its genuine customers. This would also include KROOMS denying/cancelling any bookings on account of suspected fraud transactions.</p>\n" +
            "\n" +
            "\n" +
            "<h3>INTERPRETATION NUMBER AND GENDER</h3>\n" +
            "\n" +
            "<p>The terms and conditions herein shall apply equally to both the singular and plural form of the terms defined. Whenever the context may require, any pronoun shall include the corresponding masculine, feminine and neuter form. The words \"include\", \"includes\" and \"including\" shall be deemed to be followed by the phrase \"without limitation\". Unless the context otherwise requires, the terms \"herein\", \"hereof\", \"hereto\", 'hereunder\" and words of similar import refer to this agreement as a whole.</p>\n" +
            "\n" +
            "\n" +
            "<h3>SEVERABILITY</h3>\n" +
            "\n" +
            "<p>If any provision of this agreement is determined to be invalid or unenforceable in whole or in part, such invalidity or unenforceability shall attach only to such provision or part of such provision and the remaining part of such provision and all other provisions of this Agreement shall continue to be in full force and effect.</p>\n" +
            "\n" +
            "\n" +
            "<h3>HEADINGS</h3>\n" +
            "\n" +
            "<p>The headings and subheadings herein are included for convenience and identification only and are not intended to describe, interpret, define or limit the scope, extent or intent of this agreement, terms and conditions, notices, or the right to use this website by the User contained herein or any other section or pages of KROOMS Websites or its partner websites or any provision hereof in any manner whatsoever.</p>\n" +
            "\n" +
            "<p>In the event that any of the terms, conditions, and notices contained herein conflict with the Additional Terms or other terms and guidelines contained within any particular KROOMS website, then these terms shall control.</p>\n" +
            "\n" +
            "\n" +
            "<h3>RELATIONSHIP</h3>\n" +
            "\n" +
            "<p>None of the provisions of any agreement, terms and conditions, notices, or the right to use this website by the User contained herein or any other section or pages of KROOMS Websites or its partner websites, shall be deemed to constitute a partnership between the User and KROOMS and no party shall have any authority to bind or shall be deemed to be the agent of the other in any way.</p>\n" +
            "\n" +
            "\n" +
            "<h3>UPDATION OF THE INFORMATION BY KROOMS</h3>\n" +
            "\n" +
            "<p>User acknowledges that KROOMS provides services with reasonable diligence and care. It endeavours its best to ensure that User does not face any inconvenience. However, at some times, the information, software, products, and services included in or available through the KROOMS websites or other sales channels and ad materials may include inaccuracies or typographical errors which will be immediately corrected as soon as KROOMS notices them. Changes are/may be periodically made/added to the information provided such. KROOMS may make improvements and/or changes in the KROOMS websites at any time without any notice to the User. Any advice received except through an authorized representative of KROOMS via the KROOMS websites should not be relied upon for any decisions.</p>\n" +
            "\n" +
            "\n" +
            "<h3>MODIFICATION OF THESE TERMS OF USE</h3>\n" +
            "\n" +
            "<p>KROOMS reserves the right to change the terms, conditions, and notices under which the KROOMS websites are offered, including but not limited to the charges. The User is responsible for regularly reviewing these terms and conditions.</p>\n" +
            "\n" +
            "\n" +
            "<h3>JURISDICTION</h3>\n" +
            "\n" +
            "<p>KROOMS hereby expressly disclaims any implied warranties imputed by the laws of any jurisdiction or country other than those where it is operating its offices. KROOMS considers itself and intends to be subject to the jurisdiction only of the courts of Indore, Madhya Pradesh, India.</p>\n" +
            "\n" +
            "\n" +
            "<h3>RESPONSIBILITIES OF USER VIS-Ï¿½-VIS THE AGREEMENT</h3>\n" +
            "\n" +
            "<p>The User expressly agrees that use of the services is at their sole risk. To the extent KROOMS acts only as a booking agent on behalf of third party service providers, it shall not have any liability whatsoever for any aspect of the standards of services provided by the service providers. In no circumstances shall KROOMS be liable for the services provided by the service provider. The services are provided on an \"as is\" and \"as available\" basis. KROOMS may change the features or functionality of the services at any time, in their sole discretion, without notice. KROOMS expressly disclaims all warranties of any kind, whether express or implied, including, but not limited to the implied warranties of merchantability, fitness for a particular purpose and non-infringement. No advice or information, whether oral or written, which the User obtains from KROOMS or through the services shall create any warranty not expressly made herein or in the terms and conditions of the services. If the User does not agree with any of the terms above, they are advised not to read the material on any of the KROOMS pages or otherwise use any of the contents, pages, information or any other material provided by KROOMS. The sole and exclusive remedy of the User in case of disagreement, in whole or in part, of the user agreement, is to discontinue using the services after notifying KROOMS in writing.</p>\n" +
            "\n" +
            "</ul>\n" +
            "        </div>\n" +
            "    </body>\n" +
            "</html>";

    public TermAndConditionDialog() {
    }

    public void getPerameter(FragmentActivity activity, boolean isOwner) {
        this.mFActivity = activity;
        this.mIsOwner = isOwner;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.terms_condition_dialog, container);
        TextView text = (TextView) view.findViewById(R.id.termText);
        text.setMovementMethod(new ScrollingMovementMethod());
        if (mIsOwner) {
//                    .setText(Html.fromHtml("<h1>Title</h1><br><p>Description here</p>"));.setText(ownerTerms);

            text.setText(Html.fromHtml(ownerHtmlTC));
        } else {
            text.setText(Html.fromHtml(htmlTC));
        }

        Button agreeBtn = (Button) view.findViewById(R.id.agreeBtn);
        agreeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agreeBtnClicked();
                dismiss();
            }
        });

        return view;
    }


    private static boolean permissionDialogShown = false;

    @Override
    public void show(FragmentManager manager, String tag) {
        if (permissionDialogShown) {
            return;
        }
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDialog);
        super.show(manager, tag);
        permissionDialogShown = true;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        permissionDialogShown = false;
        super.onDismiss(dialog);
    }

    public abstract void agreeBtnClicked();

}
