package hirs.attestationca.portal.page.controllers;

import hirs.attestationca.portal.page.Page;
import hirs.attestationca.portal.page.PageController;
import hirs.attestationca.portal.page.PageMessages;
import hirs.attestationca.portal.page.params.ReferenceManifestDetailsPageParams;
import hirs.attestationca.validation.SupplyChainValidationServiceImpl;
import hirs.attestationca.entity.BaseReferenceManifest;
import hirs.attestationca.entity.EventLogMeasurements;
import hirs.attestationca.entity.ReferenceDigestValue;
import hirs.attestationca.entity.ReferenceManifest;
import hirs.attestationca.entity.SupportReferenceManifest;
import hirs.attestationca.entity.SwidResource;
import hirs.attestationca.entity.certificate.CertificateAuthorityCredential;
import hirs.persist.DBManagerException;
import hirs.attestationca.service.CertificateService;
import hirs.attestationca.service.ReferenceDigestValueService;
import hirs.attestationca.service.ReferenceManifestService;
import hirs.tpm.eventlog.TCGEventLog;
import hirs.tpm.eventlog.TpmPcrEvent;
import hirs.attestationca.portal.util.ReferenceManifestValidator;
import hirs.attestationca.validation.SupplyChainCredentialValidator;
import hirs.attestationca.validation.SupplyChainValidatorException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Controller for the Reference Manifest Details page.
 */
@RestController
@RequestMapping(path = "/rim-details")
public class ReferenceManifestDetailsPageController
        extends PageController<ReferenceManifestDetailsPageParams> {

    @Autowired
    private final ReferenceManifestService referenceManifestService;
    @Autowired
    private final ReferenceDigestValueService referenceDigestValueService;
    @Autowired
    private final CertificateService certificateService;
    private static final ReferenceManifestValidator RIM_VALIDATOR
            = new ReferenceManifestValidator();
    private static final Logger LOGGER
            = LogManager.getLogger(ReferenceManifestDetailsPageController.class);

    /**
     * Constructor providing the Page's display and routing specification.
     *
     * @param referenceManifestService the reference manifest service.
     * @param referenceDigestValueService    the reference event service.
     * @param certificateService       the certificate service.
     */
    @Autowired
    public ReferenceManifestDetailsPageController(
            final ReferenceManifestService referenceManifestService,
            final ReferenceDigestValueService referenceDigestValueService,
            final CertificateService certificateService) {
        super(Page.RIM_DETAILS);
        this.referenceManifestService = referenceManifestService;
        this.referenceDigestValueService = referenceDigestValueService;
        this.certificateService = certificateService;
    }

    /**
     * Returns the filePath for the view and the data model for the page.
     *
     * @param params The object to map url parameters into.
     * @param model  The data model for the request. Can contain data from
     *               redirect.
     * @return the path for the view and data model for the page.
     */
    @Override
    public ModelAndView initPage(final ReferenceManifestDetailsPageParams params,
                                 final Model model) {
        // get the basic information to render the page
        ModelAndView mav = getBaseModelAndView();
        PageMessages messages = new PageMessages();

        // Map with the rim information
        HashMap<String, Object> data = new HashMap<>();

        // Check if parameters were set
        if (params.getId() == null) {
            String typeError = "ID was not provided";
            messages.addError(typeError);
            LOGGER.debug(typeError);
            mav.addObject(MESSAGES_ATTRIBUTE, messages);
        } else {
            try {
                UUID uuid = UUID.fromString(params.getId());
                data.putAll(getRimDetailInfo(uuid, referenceManifestService,
                        referenceDigestValueService, certificateService));
            } catch (IllegalArgumentException iaEx) {
                String uuidError = "Failed to parse ID from: " + params.getId();
                messages.addError(uuidError);
                LOGGER.error(uuidError, iaEx);
            } catch (Exception ioEx) {
                LOGGER.error(ioEx);
            }
            if (data.isEmpty()) {
                String notFoundMessage = "Unable to find RIM with ID: " + params.getId();
                messages.addError(notFoundMessage);
                LOGGER.warn(notFoundMessage);
                mav.addObject(MESSAGES_ATTRIBUTE, messages);
            } else {
                mav.addObject(INITIAL_DATA, data);
            }
        }

        // return the model and view
        return mav;
    }

    /**
     * This method takes the place of an entire class for a string builder.
     * Gathers all information and returns it for displays.
     *
     * @param uuid                     database reference for the requested RIM.
     * @param referenceManifestService the reference manifest service.
     * @param referenceDigestValueService    the reference digest value service.
     * @param certificateService       the certificate service.
     * @return mapping of the RIM information from the database.
     * @throws java.io.IOException      error for reading file bytes.
     * @throws NoSuchAlgorithmException If an unknown Algorithm is encountered.
     * @throws CertificateException     if a certificate doesn't parse.
     */
    public static HashMap<String, Object> getRimDetailInfo(final UUID uuid,
             final ReferenceManifestService referenceManifestService,
             final ReferenceDigestValueService referenceDigestValueService,
             final CertificateService certificateService)
                  throws IOException,
            CertificateException, NoSuchAlgorithmException {
        HashMap<String, Object> data = new HashMap<>();

        BaseReferenceManifest bRim = BaseReferenceManifest.select(referenceManifestService)
                .byEntityId(uuid).getRIM();

        if (bRim != null) {
            data.putAll(getBaseRimInfo(bRim, referenceManifestService, certificateService));
        }

        SupportReferenceManifest sRim = SupportReferenceManifest.select(referenceManifestService)
                .byEntityId(uuid).getRIM();

        if (sRim != null) {
            data.putAll(getSupportRimInfo(sRim, referenceManifestService));
        }

        EventLogMeasurements bios = EventLogMeasurements.select(referenceManifestService)
                .byEntityId(uuid).getRIM();

        if (bios != null) {
            data.putAll(getMeasurementsRimInfo(bios, referenceManifestService,
                    referenceDigestValueService));
        }

        return data;
    }

    /**
     * This method takes the place of an entire class for a string builder.
     * Gathers all information and returns it for displays.
     *
     * @param baseRim                  established ReferenceManifest Type.
     * @param referenceManifestService the reference manifest service.
     * @param certificateService       the certificate service.
     * @return mapping of the RIM information from the database.
     * @throws java.io.IOException      error for reading file bytes.
     * @throws NoSuchAlgorithmException If an unknown Algorithm is encountered.
     * @throws CertificateException     if a certificate doesn't parse.
     */
    private static HashMap<String, Object> getBaseRimInfo(
            final BaseReferenceManifest baseRim,
            final ReferenceManifestService referenceManifestService,
            final CertificateService certificateService)
            throws IOException, CertificateException, NoSuchAlgorithmException {
        HashMap<String, Object> data = new HashMap<>();

        // Software Identity
        data.put("swidName", baseRim.getSwidName());
        data.put("swidVersion", baseRim.getSwidVersion());
        data.put("swidTagVersion", baseRim.getSwidTagVersion());
        if (baseRim.isSwidCorpus() == 1) {
            data.put("swidCorpus", "True");
        } else {
            data.put("swidCorpus", "False");
        }
        if (baseRim.isSwidPatch()) {
            data.put("swidPatch", "True");
        } else {
            data.put("swidPatch", "False");
        }
        if (baseRim.isSwidSupplemental()) {
            data.put("swidSupplemental", "True");
        } else {
            data.put("swidSupplemental", "False");
        }
        data.put("swidTagId", baseRim.getTagId());
        // Entity
        data.put("entityName", baseRim.getEntityName());
        data.put("entityRegId", baseRim.getEntityRegId());
        data.put("entityRole", baseRim.getEntityRole());
        data.put("entityThumbprint", baseRim.getEntityThumbprint());
        // Link
        data.put("linkHref", baseRim.getLinkHref());
        data.put("linkHrefLink", "");
        for (BaseReferenceManifest bRim : BaseReferenceManifest
                .select(referenceManifestService).getRIMs()) {
            if (baseRim.getLinkHref().contains(bRim.getTagId())) {
                data.put("linkHrefLink", bRim.getId());
            }
        }
        data.put("linkRel", baseRim.getLinkRel());
        data.put("platformManufacturer", baseRim.getPlatformManufacturer());
        data.put("platformManufacturerId", baseRim.getPlatformManufacturerId());
        data.put("platformModel", baseRim.getPlatformModel());
        data.put("platformVersion", baseRim.getPlatformVersion());
        data.put("payloadType", baseRim.getPayloadType());
        data.put("colloquialVersion", baseRim.getColloquialVersion());
        data.put("edition", baseRim.getEdition());
        data.put("product", baseRim.getProduct());
        data.put("revision", baseRim.getRevision());
        data.put("bindingSpec", baseRim.getBindingSpec());
        data.put("bindingSpecVersion", baseRim.getBindingSpecVersion());
        data.put("pcUriGlobal", baseRim.getPcURIGlobal());
        data.put("pcUriLocal", baseRim.getPcURILocal());
        data.put("rimLinkHash", baseRim.getRimLinkHash());
        if (baseRim.getRimLinkHash() != null) {
            ReferenceManifest rim = BaseReferenceManifest.select(referenceManifestService)
                    .byHexDecHash(baseRim.getRimLinkHash()).getRIM();
            if (rim != null) {
                data.put("rimLinkId", rim.getId());
                data.put("linkHashValid", true);
            } else {
                data.put("linkHashValid", false);
            }
        }
        data.put("rimType", baseRim.getRimType());

        List<SwidResource> resources = baseRim.parseResource();
        TCGEventLog logProcessor = null;
        SupportReferenceManifest support = null;

        if (baseRim.getAssociatedRim() == null) {
            support = SupportReferenceManifest.select(referenceManifestService)
                    .byManufacturer(baseRim.getPlatformManufacturer())
                    .getRIM();
            if (support != null) {
                baseRim.setAssociatedRim(support.getId());
            }
        } else {
            support = SupportReferenceManifest.select(referenceManifestService)
                    .byEntityId(baseRim.getAssociatedRim()).getRIM();
        }
        // going to have to pull the filename and grab that from the DB
        // to get the id to make the link
        RIM_VALIDATOR.setRim(baseRim);
        for (SwidResource swidRes : resources) {
            if (support != null && swidRes.getHashValue()
                    .equalsIgnoreCase(support.getHexDecHash())) {
                RIM_VALIDATOR.validateSupportRimHash(support.getRimBytes(),
                        swidRes.getHashValue());
                if (RIM_VALIDATOR.isSupportRimValid()) {
                    data.put("supportRimHashValid", true);
                } else {
                    data.put("supportRimHashValid", false);
                }
                break;
            }
        }

        data.put("associatedRim", baseRim.getAssociatedRim());
        data.put("swidFiles", resources);
        if (support != null && (!baseRim.isSwidSupplemental()
                && !baseRim.isSwidPatch())) {
            data.put("pcrList", support.getExpectedPCRList());
        }

        Set<CertificateAuthorityCredential> certificates =
                CertificateAuthorityCredential.select(certificateService)
                        .getCertificates();
        //Report invalid signature unless RIM_VALIDATOR validates it and cert path is valid
        data.put("signatureValid", false);
        for (CertificateAuthorityCredential cert : certificates) {
            SupplyChainValidationServiceImpl scvsImpl =
                    new SupplyChainValidationServiceImpl(certificateService);
            KeyStore keystore = scvsImpl.getCaChain(cert);
            if (RIM_VALIDATOR.validateXmlSignature(cert)) {
                try {
                    if (SupplyChainCredentialValidator.verifyCertificate(
                            cert.getX509Certificate(), keystore)) {
                        data.replace("signatureValid", true);
                        break;
                    }
                } catch (SupplyChainValidatorException e) {
                    LOGGER.error("Error verifying cert chain: " + e.getMessage());
                }
            }
        }
        data.put("skID", RIM_VALIDATOR.getSubjectKeyIdentifier());
        try {
            for (CertificateAuthorityCredential cert : certificates) {
                if (Arrays.equals(cert.getEncodedPublicKey(),
                        RIM_VALIDATOR.getPublicKey().getEncoded())) {
                    data.put("issuerID", cert.getId().toString());
                }
            }
        } catch (NullPointerException e) {
            LOGGER.error("Unable to link signing certificate: " + e.getMessage());
        }
        return data;
    }

    /**
     * This method takes the place of an entire class for a string builder.
     * Gathers all information and returns it for displays.
     *
     * @param support                  established ReferenceManifest Type.
     * @param referenceManifestService the reference manifest service.
     * @return mapping of the RIM information from the database.
     * @throws java.io.IOException      error for reading file bytes.
     * @throws NoSuchAlgorithmException If an unknown Algorithm is encountered.
     * @throws CertificateException     if a certificate doesn't parse.
     */
    private static HashMap<String, Object> getSupportRimInfo(
            final SupportReferenceManifest support,
            final ReferenceManifestService referenceManifestService)
            throws IOException, CertificateException, NoSuchAlgorithmException {
        HashMap<String, Object> data = new HashMap<>();
        EventLogMeasurements measurements = null;

        if (support.getAssociatedRim() == null) {
            Set<BaseReferenceManifest> baseRims = BaseReferenceManifest
                    .select(referenceManifestService)
                    .byRimType(ReferenceManifest.BASE_RIM).getRIMs();
            for (BaseReferenceManifest baseRim : baseRims) {
                if (baseRim != null && baseRim.getAssociatedRim() != null
                        && baseRim.getAssociatedRim().equals(support.getId())) {
                    support.setAssociatedRim(baseRim.getId());
                    try {
                        referenceManifestService.updateReferenceManifest(support);
                    } catch (DBManagerException ex) {
                        LOGGER.error("Failed to update Support RIM", ex);
                    }
                    break;
                }
            }
        }

        // testing this independent of the above if statement because the above
        // starts off checking if associated rim is null; that is irrelevant for
        // this statement.
        measurements = EventLogMeasurements.select(referenceManifestService)
                .byHexDecHash(support.getHexDecHash()).getRIM();

        if (support.isSwidPatch()) {
            data.put("swidPatch", "True");
        } else {
            data.put("swidPatch", "False");
        }
        if (support.isSwidSupplemental()) {
            data.put("swidSupplemental", "True");
        } else {
            data.put("swidSupplemental", "False");
        }
        data.put("swidBase", (!support.isSwidPatch()
                && !support.isSwidSupplemental()));
        data.put("baseRim", support.getTagId());
        data.put("associatedRim", support.getAssociatedRim());
        data.put("rimType", support.getRimType());
        data.put("tagId", support.getTagId());

        TCGEventLog logProcessor = new TCGEventLog(support.getRimBytes());
        LinkedList<TpmPcrEvent> tpmPcrEvents = new LinkedList<>();
        TCGEventLog measurementsProcess;
        if (measurements != null) {
            measurementsProcess = new TCGEventLog((measurements.getRimBytes()));
            HashMap<String, TpmPcrEvent> digestMap = new HashMap<>();
            for (TpmPcrEvent tpe : logProcessor.getEventList()) {
                digestMap.put(tpe.getEventDigestStr(), tpe);
                if (!support.isSwidSupplemental()
                        && !tpe.eventCompare(
                        measurementsProcess.getEventByNumber(
                                tpe.getEventNumber()))) {
                    tpe.setError(true);
                }
                tpmPcrEvents.add(tpe);
            }
            for (TpmPcrEvent tpe : logProcessor.getEventList()) {
                tpe.setError(!digestMap.containsKey(tpe.getEventDigestStr()));
            }
            data.put("events", tpmPcrEvents);
        } else {
            data.put("events", logProcessor.getEventList());
        }

        getEventSummary(data, logProcessor.getEventList());
        return data;
    }

    private static void getEventSummary(final HashMap<String, Object> data,
                                        final Collection<TpmPcrEvent> eventList) {
        boolean crtm = false;
        boolean bootManager = false;
        boolean osLoader = false;
        boolean osKernel = false;
        boolean acpiTables = false;
        boolean smbiosTables = false;
        boolean gptTable = false;
        boolean bootOrder = false;
        boolean defaultBootDevice = false;
        boolean secureBoot = false;
        boolean pk = false;
        boolean kek = false;
        boolean sigDb = false;
        boolean forbiddenDbx = false;

        String contentStr;
        for (TpmPcrEvent tpe : eventList) {
            contentStr = tpe.getEventContentStr();
            // check for specific events
            if (contentStr.contains("CRTM")) {
                crtm = true;
            } else if (contentStr.contains("shimx64.efi")
                    || contentStr.contains("bootmgfw.efi")) {
                bootManager = true;
            } else if (contentStr.contains("grubx64.efi")
                    || contentStr.contains("winload.efi")) {
                osLoader = true;
            } else if (contentStr.contains("vmlinuz")
                    || contentStr.contains("ntoskrnl.exe")) {
                osKernel = true;
            } else if (contentStr.contains("ACPI")) {
                acpiTables = true;
            } else if (contentStr.contains("SMBIOS")) {
                smbiosTables = true;
            } else if (contentStr.contains("GPT")) {
                gptTable = true;
            } else if (contentStr.contains("BootOrder")) {
                bootOrder = true;
            } else if (contentStr.contains("Boot0000")) {
                defaultBootDevice = true;
            } else if (contentStr.contains("variable named PK")) {
                pk = true;
            } else if (contentStr.contains("variable named KEK")) {
                kek = true;
            } else if (contentStr.contains("variable named db")) {
                if (contentStr.contains("dbx")) {
                    forbiddenDbx = true;
                } else {
                    sigDb = true;
                }
            } else if (contentStr.contains("Secure Boot is enabled")) {
                secureBoot = true;
            }
        }

        data.put("crtm", crtm);
        data.put("bootManager", bootManager);
        data.put("osLoader", osLoader);
        data.put("osKernel", osKernel);
        data.put("acpiTables", acpiTables);
        data.put("smbiosTables", smbiosTables);
        data.put("gptTable", gptTable);
        data.put("bootOrder", bootOrder);
        data.put("defaultBootDevice", defaultBootDevice);
        data.put("secureBoot", secureBoot);
        data.put("pk", pk);
        data.put("kek", kek);
        data.put("sigDb", sigDb);
        data.put("forbiddenDbx", forbiddenDbx);
    }

    /**
     * This method takes the place of an entire class for a string builder.
     * Gathers all information and returns it for displays.
     *
     * @param measurements             established ReferenceManifest Type.
     * @param referenceManifestService the reference manifest service.
     * @param referenceDigestValueService    the reference digest value service.
     * @return mapping of the RIM information from the database.
     * @throws java.io.IOException      error for reading file bytes.
     * @throws NoSuchAlgorithmException If an unknown Algorithm is encountered.
     * @throws CertificateException     if a certificate doesn't parse.
     */
    private static HashMap<String, Object> getMeasurementsRimInfo(
            final EventLogMeasurements measurements,
            final ReferenceManifestService referenceManifestService,
            final ReferenceDigestValueService referenceDigestValueService)
            throws IOException, CertificateException, NoSuchAlgorithmException {
        HashMap<String, Object> data = new HashMap<>();
        LinkedList<TpmPcrEvent> livelogEvents = new LinkedList<>();
        BaseReferenceManifest base = null;
        List<SupportReferenceManifest> supports = new ArrayList<>();
        SupportReferenceManifest baseSupport = null;

        data.put("supportFilename", "Blank");
        data.put("supportId", "");
        data.put("associatedRim", "");
        data.put("rimType", measurements.getRimType());
        data.put("hostName", measurements.getDeviceName());
        data.put("validationResult", measurements.getOverallValidationResult());
        data.put("swidBase", true);

        List<ReferenceDigestValue> eventValues = new ArrayList<>();
        if (measurements.getDeviceName() != null) {
            supports.addAll(SupportReferenceManifest
                    .select(referenceManifestService)
                    .byDeviceName(measurements
                            .getDeviceName()).getRIMs());
            for (SupportReferenceManifest support : supports) {
                if (support.isBaseSupport()) {
                    baseSupport = support;
                }
            }

            if (baseSupport != null) {
                data.put("supportFilename", baseSupport.getFileName());
                data.put("supportId", baseSupport.getId());

                base = BaseReferenceManifest
                        .select(referenceManifestService)
                        .byEntityId(baseSupport.getAssociatedRim())
                        .getRIM();
                data.put("tagId", baseSupport.getTagId());

                if (base != null) {
                    data.put("associatedRim", base.getId());
                    // this was moved from outside of the null check
                    // for a reason I believe
                    eventValues.addAll(referenceDigestValueService
                            .getValuesByBaseRimId(base.getId()));
                }
            }
        }

        TCGEventLog measurementLog = new TCGEventLog(measurements.getRimBytes());
        Map<String, ReferenceDigestValue> eventValueMap = new HashMap<>();

        for (ReferenceDigestValue rdv : eventValues) {
            eventValueMap.put(rdv.getDigestValue(), rdv);
        }
        for (TpmPcrEvent measurementEvent : measurementLog.getEventList()) {
            if (!eventValueMap.containsKey(measurementEvent.getEventDigestStr())) {
                livelogEvents.add(measurementEvent);
            }
        }

        if (!supports.isEmpty()) {
            Map<String, List<TpmPcrEvent>> baselineLogEvents = new HashMap<>();
            List<TpmPcrEvent> matchedEvents = null;
            List<TpmPcrEvent> combinedBaselines = new LinkedList<>();
            for (SupportReferenceManifest support : supports) {
                combinedBaselines.addAll(support.getEventLog());
            }
            String bootVariable;
            String variablePrefix = "Variable Name:";
            String variableSuffix = "UEFI_GUID";
            for (TpmPcrEvent tpe : livelogEvents) {
                matchedEvents = new ArrayList<>();
                for (TpmPcrEvent tpmPcrEvent : combinedBaselines) {
                    if (tpmPcrEvent.getEventType() == tpe.getEventType()) {
                        if (tpe.getEventContentStr().contains(variablePrefix)) {
                            bootVariable = tpe.getEventContentStr().substring((
                                            tpe.getEventContentStr().indexOf(variablePrefix)
                                                    + variablePrefix.length()),
                                    tpe.getEventContentStr().indexOf(variableSuffix));
                            if (tpmPcrEvent.getEventContentStr().contains(bootVariable)) {
                                matchedEvents.add(tpmPcrEvent);
                            }
                        } else {
                            matchedEvents.add(tpmPcrEvent);
                        }
                    }
                }
                baselineLogEvents.put(tpe.getEventDigestStr(), matchedEvents);
            }
            data.put("eventTypeMap", baselineLogEvents);
        }

        TCGEventLog logProcessor = new TCGEventLog(measurements.getRimBytes());
        data.put("livelogEvents", livelogEvents);
        data.put("events", logProcessor.getEventList());
        getEventSummary(data, logProcessor.getEventList());

        return data;
    }
}
