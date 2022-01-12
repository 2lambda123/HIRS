#!/bin/bash

# This file will set the tpm emulators PCR values to match the dell8850 event log.

tpm2_pcrextend 0:sha256=38dc62a7c4ba6f19930538c1704b5a97f20f19e802951aab7e78ced610a3df5f -Q
tpm2_pcrextend 0:sha256=d4720b4009438213b803568017f903093f6bea8ab47d283db32b6eabedbbf155 -Q
tpm2_pcrextend 0:sha256=2649fffc46f2044e2d683712fb59ce10ccfcbeb91d541cbe117d9c2d459da273 -Q
tpm2_pcrextend 0:sha256=df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119 -Q


tpm2_pcrextend 1:sha256=cbb15df37b131921890da0973ed097d567123b08e3fa6449e33a6acd15385be0 -Q
tpm2_pcrextend 1:sha256=df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119 -Q
tpm2_pcrextend 1:sha256=4e93b6abf5532ff7a4da93769c41874f62cef02a9abc60b6baa62227762e5964 -Q
tpm2_pcrextend 1:sha256=9ad0d8e4e4b6b80386f43e747d0e8f4a55a860bae1fbbf54c588fd474b30a1da -Q
tpm2_pcrextend 1:sha256=fd662842e607c5800389f2d3073cb26100ce4b5f93d9e62e6b139813141a4173 -Q

tpm2_pcrextend 2:sha256=df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119 -Q

tpm2_pcrextend 3:sha256=df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119 -Q

tpm2_pcrextend 4:sha256=df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119 -Q
tpm2_pcrextend 4:sha256=dda0121dcf167db1e2622d10f454701837ac6af304a03ec06b3027904988c56b -Q
tpm2_pcrextend 4:sha256=afb8038e914c99969dd828b58289ff2f820fb785025f21a92cc48651ebc13005 -Q
tpm2_pcrextend 4:sha256=f80bdf3a58ec348742486e439f3c75a962043931f7cacd1e9bb8e6bf0cb2df9a -Q


tpm2_pcrextend 5:sha256=df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119 -Q
tpm2_pcrextend 5:sha256=ef4c76c6a2226cb891be17a65f5a3035889979b5a1b1a246224ee7120dda3efa -Q

tpm2_pcrextend 6:sha256=df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119 -Q

tpm2_pcrextend 7:sha256=ccfc4bb32888a345bc8aeadaba552b627d99348c767681ab3141f5b01e40a40e -Q
tpm2_pcrextend 7:sha256=2abfe9865a654102acb12f0fefe52dc4d01bce40901410eb3dadaf212700a2b7 -Q
tpm2_pcrextend 7:sha256=63a525134bfbc242058c0e6b42794f8b1d142d13029a9aa38a3272c5ca2390c5 -Q
tpm2_pcrextend 7:sha256=ad1850a4885628d86273bad743779c9e665db060236270b5d24dd98f3a22fe86 -Q
tpm2_pcrextend 7:sha256=f0bf49c6a2d3e170077f1f66875d6cb9b2aa382060cac5c0b645660bb95bc058 -Q
tpm2_pcrextend 7:sha256=df3f619804a92fdb4057192dc43dd748ea778adc52bc498ce80524c014b81119 -Q
tpm2_pcrextend 7:sha256=4d4a8e2c74133bbdc01a16eaf2dbb5d575afeb36f5d8dfcf609ae043909e2ee9 -Q
tpm2_pcrextend 7:sha256=87ee47938723178072c0b0ed3ff7575e82ca37f0634a1a67d15d4d5ce53e8dab -Q
tpm2_pcrextend 7:sha256=194c8cf6648963b6574271d6c86d250a381ea0346749a355576fa95f5b6e1dae -Q

tpm2_pcrextend 14:sha256=9fa163e06ff76a9f0d0262151328aa38f903495bc81ebcbd5bc40fcdbebb4a63 -Q
tpm2_pcrextend 14:sha256=8d8a3aae50d5d25838c95c034aadce7b548c9a952eb7925e366eda537c59c3b0 -Q

