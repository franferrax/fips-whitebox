/*
 * Copyright (c) 2022, Red Hat, Inc.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import sun.security.pkcs11.P11Helper;
import sun.security.pkcs11.SunPKCS11;
import sun.security.pkcs11.wrapper.CK_ATTRIBUTE;
import sun.security.pkcs11.wrapper.PKCS11;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAUtil;

import java.math.BigInteger;
import java.security.interfaces.RSAPrivateCrtKey;

import static sun.security.pkcs11.P11Helper.getObjSession;
import static sun.security.pkcs11.P11Helper.releaseSession;
import static sun.security.pkcs11.wrapper.PKCS11Constants.*;
import static sun.security.pkcs11.wrapper.PKCS11Constants.CKA_COEFFICIENT;
import static support.Assert.assertEquals;
import static support.JCAInit.initJCA;

/*
 * @test
 * @modules jdk.crypto.cryptoki/sun.security.pkcs11:+open
 *          jdk.crypto.cryptoki/sun.security.pkcs11.wrapper:+open
 *          java.base/sun.security.rsa:+open
 * @library support
 * @compile/module=jdk.crypto.cryptoki sun/security/pkcs11/P11Helper.java
 * @run main/othervm -Dcom.redhat.fips=true ExportRSATest
 */
public class ExportRSATest {
    public static void main(String[] args) throws Exception {
        SunPKCS11 sunp11 = initJCA();
        PKCS11 p11 = P11Helper.getP11(sunp11);
        long session = getObjSession(sunp11);


        RSAPrivateCrtKey key = (RSAPrivateCrtKey) RSAPrivateCrtKeyImpl.newKey(
                RSAUtil.KeyType.RSA,
                null,
                new BigInteger("31000296822646386998584161883868828760659721574044842402848806623672928074354715268502355519006545105567880858399197517290835313537026014607077524770377840766842604795117305069474843522905644090561429291565496868709053954136947264739356903926845185448323958328335200363237832476813215346720672072534967200096026119425179694268819250260499153729988739164619747683161611644351681214206887307330001040506009355564941750161859389976399069883751802040038840118519066063258553214379913795552720677334462446599648697357214664819560828909026738415884372844699912951846755690286141944255888656339408190200445386621584970932613"),
                new BigInteger("65537"),
                new BigInteger("10063495657442389541707402598216417167142767848510063355365798875728848509725751366974191886520045884324223953834367260942101733303328325385134722973126456235631420678641388305126528464070945847791238661794954695542748720177358637980527307185919882210249053411589352392204173000659187275912573025057317655401307089662747841468892585296391253665444610446164288716057027297202701806241656337154940674570480144564782064973604125601357111627417430393962339684270233104865244361883888489436881286938803420266136193811208329438902443740862371942632626013828079856312204125846703456025860194078539962300717728976659395077573"),
                new BigInteger("179274130269001839205999133545996657215250561140229107938161917349296752985394846924797498079799544204628553605406228889319847402548833793223314768267306081575440333742641843553705624817320953058234448220463554871853064049350690832771076979281925556065848155038492847773302501253795184815867518138871405993819"),
                new BigInteger("172921194910443952677252886314210352574032208167227642944747119398461158017758906086832356088933325609802165196871794246367540942962961867068881125198022402667671294513445868228162460173622821203845848017824657001181350972724354515930479834007840766256445876229298713518224249594413310470258817768573646010527"),
                new BigInteger("67273275946496822124801817161247780504061172316731837763755070164506386410269244402727388654905018396088164225059972032211468440283862085184567192517171345410757345129206263004352998018407528549691926470327604329821053819455903377486596825479963916571210212198630614847930610393130368801999326694680325428475"),
                new BigInteger("127939573370958646586925175160439839876134820663452724090938600676136885898394976841241041158372950020502879109985449917025736496686315485486106731765672952423100132744289129873435914542301249330199449540508411940282321853554957140591620560465175258480076965568800300895451052666181042943564395910407365631695"),
                new BigInteger("98115720565758610794530818682238754380394383627437733527694535704315028691058186696476388021702450938158718557147776438778988623657810767441961659758069048196734742357031856905485672286714699728823916447071787274780461960210639540226507930338653529208036523123449611053237419418935258671659389259217746438177")
        );

        long keyId = p11.C_CreateObject(session, new CK_ATTRIBUTE[]{
                new CK_ATTRIBUTE(CKA_CLASS, CKO_PRIVATE_KEY),
                new CK_ATTRIBUTE(CKA_KEY_TYPE, CKK_RSA),
                new CK_ATTRIBUTE(CKA_MODULUS, key.getModulus()),
                new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT, key.getPublicExponent()),
                new CK_ATTRIBUTE(CKA_PRIVATE_EXPONENT, key.getPrivateExponent()),
                new CK_ATTRIBUTE(CKA_PRIME_1, key.getPrimeP()),
                new CK_ATTRIBUTE(CKA_PRIME_2, key.getPrimeQ()),
                new CK_ATTRIBUTE(CKA_EXPONENT_1, key.getPrimeExponentP()),
                new CK_ATTRIBUTE(CKA_EXPONENT_2, key.getPrimeExponentQ()),
                new CK_ATTRIBUTE(CKA_COEFFICIENT, key.getCrtCoefficient())
        });

        CK_ATTRIBUTE[] exportAttrs = {
                new CK_ATTRIBUTE(CKA_MODULUS, new byte[0]),
                new CK_ATTRIBUTE(CKA_PUBLIC_EXPONENT, new byte[0]),
                new CK_ATTRIBUTE(CKA_PRIVATE_EXPONENT, new byte[0]),
                new CK_ATTRIBUTE(CKA_PRIME_1, new byte[0]),
                new CK_ATTRIBUTE(CKA_PRIME_2, new byte[0]),
                new CK_ATTRIBUTE(CKA_EXPONENT_1, new byte[0]),
                new CK_ATTRIBUTE(CKA_EXPONENT_2, new byte[0]),
                new CK_ATTRIBUTE(CKA_COEFFICIENT, new byte[0]),
        };
        p11.C_GetAttributeValue(session, keyId, exportAttrs);

        int i = 0;
        assertEquals("Exported modulus", key.getModulus(), exportAttrs[i++].getBigInteger());
        assertEquals("Exported public exponent", key.getPublicExponent(), exportAttrs[i++].getBigInteger());
        assertEquals("Exported private exponent", key.getPrivateExponent(), exportAttrs[i++].getBigInteger());
        assertEquals("Exported prime P", key.getPrimeP(), exportAttrs[i++].getBigInteger());
        assertEquals("Exported prime Q", key.getPrimeQ(), exportAttrs[i++].getBigInteger());
        assertEquals("Exported prime exponent P", key.getPrimeExponentP(), exportAttrs[i++].getBigInteger());
        assertEquals("Exported prime exponent Q", key.getPrimeExponentQ(), exportAttrs[i++].getBigInteger());
        assertEquals("Exported coefficient", key.getCrtCoefficient(), exportAttrs[i++].getBigInteger());

        releaseSession(sunp11, session);
    }
}
