package org.robolectric.fakes;

import android.content.Context;
import android.util.AttributeSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.R;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.TestRunners;
import org.robolectric.res.Attribute;
import org.robolectric.res.ResName;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.robolectric.res.Attribute.ANDROID_RES_NS_PREFIX;
import static org.robolectric.util.TestUtil.TEST_PACKAGE;
import static org.robolectric.util.TestUtil.TEST_PACKAGE_NS;

@RunWith(TestRunners.WithDefaults.class)
public class RoboAttributeSetTest {

  private static final String ANDROID_NS = "http://schemas.android.com/apk/res/android";

  private AttributeSet roboAttributeSet;
  private Context context;

  @Before
  public void setUp() throws Exception {
    context = RuntimeEnvironment.application;
  }

  @Test
  public void getSystemAttributeResourceValue_shouldReturnTheResourceValue() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("android:attr/text"), "@android:string/ok", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_NS, "text", 0)).isEqualTo(android.R.string.ok);
  }

  @Test
  public void getAttributeResourceValue_shouldReturnTheResourceValue() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("android:attr/text"), "@string/ok", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_NS, "text", 0)).isEqualTo(R.string.ok);
  }

  @Test
  public void getSystemAttributeResourceValueWithLeadingWhitespace_shouldReturnTheResourceValue() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("android:attr/text"), " @android:string/ok", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_NS, "text", 0)).isEqualTo(android.R.string.ok);
  }

  @Test
  public void getAttributeResourceValueWithLeadingWhitespace_shouldReturnTheResourceValue() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("android:attr/text"), " @string/ok", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_NS, "text", 0)).isEqualTo(R.string.ok);
  }

  @Test
  public void getSystemAttributeResourceValue_shouldNotReturnTheResourceValueIfNameSpaceDoesNotMatch() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("com.another.domain:attr/text"), "@android:string/ok", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_NS, "text", 0)).isEqualTo(0);
  }

  @Test
  public void getAttributeResourceValue_shouldReturnTheResourceValueFromSystemNamespace() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("com.another.domain:attr/text"), "@android:string/ok", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_RES_NS_PREFIX + "com.another.domain", "text", 0)).isEqualTo(android.R.string.ok);
  }

  @Test
  public void getSystemAttributeResourceValue_shouldReturnDefaultValueForNullResourceId() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("android:attr/text"), "@null", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_RES_NS_PREFIX + "com.some.namespace", "text", 0)).isEqualTo(0);
  }

  @Test
  public void getSystemAttributeResourceValue_shouldReturnValueForMatchingNamespace() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("com.some.namespace:attr/id"), "@id/burritos", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_RES_NS_PREFIX + "com.some.namespace", "id", 0)).isEqualTo(R.id.burritos);
  }

  @Test
  public void getSystemAttributeResourceValue_shouldReturnDefaultValueForNonMatchingNamespaceId() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("com.some.namespace:attr/id"), "@id/burritos", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_RES_NS_PREFIX + "com.some.other.namespace", "id", 0)).isEqualTo(0);
  }

  @Test
  public void shouldCopeWithDefiningLocalIds() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("android:attr/id"), "@+id/text1", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(ANDROID_NS, "id", 0)).isEqualTo(R.id.text1);
  }

  @Test
  public void getAttributeResourceValue_withNamespace_shouldReturnTheResourceValue() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/message"), "@string/howdy", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(TEST_PACKAGE_NS, "message", 0)).isEqualTo(R.string.howdy);
  }

  @Test
  public void getAttributeResourceValue_shouldReturnDefaultValueWhenAttributeIsNull() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/message"), "@null", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeResourceValue(TEST_PACKAGE_NS, "message", -1)).isEqualTo(-1);
  }

  @Test
  public void getAttributeResourceValue_shouldReturnDefaultValueWhenNotInAttributeSet() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context);
    assertThat(roboAttributeSet.getAttributeResourceValue(TEST_PACKAGE_NS, "message", -1)).isEqualTo(-1);
  }

  @Test
  public void getAttributeBooleanValue_shouldGetBooleanValuesFromAttributes() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/isSugary"), "true", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeBooleanValue(TEST_PACKAGE_NS, "isSugary", false)).isTrue();
  }

  @Test
  public void getAttributeBooleanValue_withNamespace_shouldGetBooleanValuesFromAttributes() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("xxx:attr/isSugary"), "true", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeBooleanValue(ANDROID_RES_NS_PREFIX + "xxx", "isSugary", false)).isTrue();
  }

  @Test
  public void getAttributeBooleanValue_shouldReturnDefaultBooleanValueWhenNotInAttributeSet() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context);
    assertThat(roboAttributeSet.getAttributeBooleanValue(ANDROID_RES_NS_PREFIX + "com.some.namespace", "isSugary", true)).isTrue();
  }

  @Test
  public void getAttributeValue_byName_shouldReturnValueFromAttribute() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/isSugary"), "oh heck yeah", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeValue(TEST_PACKAGE_NS, "isSugary")).isEqualTo("oh heck yeah");
  }

  @Test
  public void getAttributeValue_byNameWithReference_shouldReturnFullyQualifiedValueFromAttribute() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/isSugary"), "@string/ok", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeValue(TEST_PACKAGE_NS, "isSugary")).isEqualTo("@org.robolectric:string/ok");
  }

  @Test
  public void getAttributeValue_byId_shouldReturnValueFromAttribute() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/isSugary"), "oh heck yeah", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeValue(0)).isEqualTo("oh heck yeah");
  }

  @Test
  public void getAttributeValue_byIdWithReference_shouldReturnValueFromAttribute() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/isSugary"), "@string/ok", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeValue(0)).isEqualTo("@org.robolectric:string/ok");
  }

  @Test
  public void getAttributeIntValue_shouldReturnValueFromAttribute() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/sugarinessPercent"), "100", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeIntValue(TEST_PACKAGE_NS, "sugarinessPercent", 0)).isEqualTo(100);
  }

  @Test
  public void getAttributeIntValue_shouldReturnHexValueFromAttribute() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/sugarinessPercent"), "0x10", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeIntValue(TEST_PACKAGE_NS, "sugarinessPercent", 0)).isEqualTo(16);
  }

  @Test
  public void getAttributeIntValue_whenTypeAllowsIntOrEnum_withInt_shouldReturnInt() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/numColumns"), "3", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeIntValue(TEST_PACKAGE_NS, "numColumns", 0)).isEqualTo(3);
  }

  @Test
  public void getAttributeValue_shouldReturnAttributeAssociatedWithResourceId() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("ns:attr/textStyle2"), "expected value", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeValue(0)).isEqualTo("expected value");
  }

  @Test
  public void getAttributeIntValue_shouldReturnValueFromAttributeWhenNotInAttributeSet() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context);
    assertThat(roboAttributeSet.getAttributeIntValue(TEST_PACKAGE_NS, "sugarinessPercent", 42)).isEqualTo(42);
  }

  @Test
  public void getAttributeIntValue_shouldReturnEnumValuesForEnumAttributesWhenNotInAttributeSet() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context);
    assertThat(roboAttributeSet.getAttributeIntValue(TEST_PACKAGE_NS, "itemType", 24)).isEqualTo(24);
  }

  @Test
  public void getAttributeFloatValue_shouldGetFloatValuesFromAttributes() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE + ":attr/sugaryScale"), "1234.456", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeFloatValue(TEST_PACKAGE_NS, "sugaryScale", 78.9f)).isEqualTo(1234.456f);
  }

  @Test
  public void getAttributeFloatValue_withNamespace_shouldGetFloatValuesFromAttributes() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("xxx:attr/sugaryScale"), "1234.456", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getAttributeFloatValue(ANDROID_RES_NS_PREFIX + "xxx", "sugaryScale", 78.9f)).isEqualTo(1234.456f);
  }

  @Test
  public void getAttributeFloatValue_shouldReturnDefaultFloatValueWhenNotInAttributeSet() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context);
    assertThat(roboAttributeSet.getAttributeFloatValue(TEST_PACKAGE_NS, "sugaryScale", 78.9f)).isEqualTo(78.9f);
  }

  @Test
  public void getStyleAttribute_doesNotThrowException() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context);
    roboAttributeSet.getStyleAttribute();
  }

  @Test
  public void getStyleAttribute_returnsZeroWhenNoStyle() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context);
    assertThat(roboAttributeSet.getStyleAttribute()).isEqualTo(0);
  }

  @Test
  public void getStyleAttribute_returnsCorrectValue() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(":attr/style"), "@style/FancyStyle", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getStyleAttribute()).isEqualTo(R.style.FancyStyle);
  }

  @Test
  public void getStyleAttribute_doesNotThrowException_whenStyleIsBogus() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName(TEST_PACKAGE, "attr", "style"), "@style/bogus_style", TEST_PACKAGE)
    );
    assertThat(roboAttributeSet.getStyleAttribute()).isEqualTo(0);
  }

  @Test
  public void getAttributeNameResource() throws Exception {
    roboAttributeSet = RoboAttributeSet.create(context,
        new Attribute(new ResName("org.robolectric.lib1:attr/offsetX"), "1", TEST_PACKAGE),
        new Attribute(new ResName("org.robolectric.lib1:attr/offsetY"), "1", TEST_PACKAGE));
    assertThat(roboAttributeSet.getAttributeNameResource(0)).isEqualTo(org.robolectric.lib1.R.attr.offsetX); // no id for attr.message for some reason...
    assertThat(roboAttributeSet.getAttributeNameResource(1)).isEqualTo(org.robolectric.lib1.R.attr.offsetY);
  }

  @Test
  public void shouldCreateRoboAttributeSetFromVarargs() {
    Context context = RuntimeEnvironment.application;
    ResName resName = new ResName("android", "attr", "orientation");
    String attrValue = "vertical";
    String contextPackageName = context.getPackageName();

    ArrayList<Attribute> attributes = new ArrayList<>();
    attributes.add(new Attribute(resName, attrValue, contextPackageName));

    AttributeSet attributeSet = RoboAttributeSet.create(context, attributes);

    assertThat(attributeSet.getAttributeCount()).isEqualTo(1);
    assertThat(attributeSet.getAttributeName(0)).isEqualTo("android:orientation");
    assertThat(attributeSet.getAttributeValue(0)).isEqualTo(attrValue);
  }

  @Test
  public void shouldCreateRoboAttributeSetFromList() {
    Context context = RuntimeEnvironment.application;
    ResName resName = new ResName("android", "attr", "orientation");
    String attrValue = "vertical";
    String contextPackageName = context.getPackageName();

    Attribute attribute = new Attribute(resName, attrValue, contextPackageName);

    AttributeSet attributeSet = RoboAttributeSet.create(context, attribute);

    assertThat(attributeSet.getAttributeCount()).isEqualTo(1);
    assertThat(attributeSet.getAttributeName(0)).isEqualTo("android:orientation");
    assertThat(attributeSet.getAttributeValue(0)).isEqualTo(attrValue);
  }
}
