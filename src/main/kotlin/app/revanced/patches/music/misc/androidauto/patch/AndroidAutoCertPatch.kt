package app.revanced.patches.music.misc.androidauto.patch

import app.revanced.extensions.toErrorResult
import app.revanced.patcher.annotation.Description
import app.revanced.patcher.annotation.Name
import app.revanced.patcher.annotation.Version
import app.revanced.patcher.data.BytecodeContext
import app.revanced.patcher.extensions.addInstruction
import app.revanced.patcher.extensions.replaceInstruction
import app.revanced.patcher.patch.BytecodePatch
import app.revanced.patcher.patch.PatchResult
import app.revanced.patcher.patch.PatchResultSuccess
import app.revanced.patcher.patch.annotations.Patch
import app.revanced.patches.music.misc.androidauto.fingerprints.SHACertificateCheckFingerprint
import app.revanced.patches.shared.annotation.YouTubeMusicCompatibility


@Patch
@Name("android-auto-cert-patch")
@Description("Enable youtube music in android auto in case of non-root version")
@YouTubeMusicCompatibility
@Version("0.0.1")
class AndroidAutoCertPatch : BytecodePatch(
    listOf(
        SHACertificateCheckFingerprint
    )
) {
    override fun execute(context: BytecodeContext): PatchResult {
        SHACertificateCheckFingerprint.result?.let {
            with(it.mutableMethod) {
                val replaceIndex = it.scanResult.stringsScanResult?.matches!!.component4().index + 2
                replaceInstruction(replaceIndex + 2, "const/4 p1, 0x1")
                addInstruction(replaceIndex + 3,"return v0")
            }
        } ?: return SHACertificateCheckFingerprint.toErrorResult()

        return PatchResultSuccess()
    }
}
