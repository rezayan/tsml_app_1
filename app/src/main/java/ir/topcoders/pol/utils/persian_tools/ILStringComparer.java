package ir.topcoders.pol.utils.persian_tools;

import android.util.SparseIntArray;

import java.util.Locale;

public class ILStringComparer
{
	private final static char	NUMBERS_FIRST_CHAR			= 0x0030;
	private final static char	NUMBERS_LAST_CHAR			= 0x0039;
	private final static char	ENGLISH_SMALL_FIRST_CHAR	= 0x0061;
	private final static char	ENGLISH_SMALL_END_CHAR		= 0x007A;
	SparseIntArray m_CharCompareMap;

	public ILStringComparer()
	{
		m_CharCompareMap = new SparseIntArray();

		m_CharCompareMap.append(0x622, 10);
		m_CharCompareMap.append(0x623, 11);
		m_CharCompareMap.append(0x625, 12);
		m_CharCompareMap.append(0x626, 44);
		m_CharCompareMap.append(0x627, 13);
		m_CharCompareMap.append(0x628, 14);
		m_CharCompareMap.append(0x629, 43);
		m_CharCompareMap.append(0x62A, 16);
		m_CharCompareMap.append(0x62B, 17);
		m_CharCompareMap.append(0x62C, 18);
		m_CharCompareMap.append(0x62D, 20);
		m_CharCompareMap.append(0x62E, 21);
		m_CharCompareMap.append(0x62F, 22);
		m_CharCompareMap.append(0x630, 23);
		m_CharCompareMap.append(0x631, 24);
		m_CharCompareMap.append(0x632, 25);
		m_CharCompareMap.append(0x633, 27);
		m_CharCompareMap.append(0x634, 28);
		m_CharCompareMap.append(0x635, 29);
		m_CharCompareMap.append(0x636, 30);
		m_CharCompareMap.append(0x637, 31);
		m_CharCompareMap.append(0x638, 32);
		m_CharCompareMap.append(0x639, 33);
		m_CharCompareMap.append(0x63A, 34);
		m_CharCompareMap.append(0x641, 35);
		m_CharCompareMap.append(0x642, 36);
		m_CharCompareMap.append(0x643, 37);
		m_CharCompareMap.append(0x644, 39);
		m_CharCompareMap.append(0x645, 40);
		m_CharCompareMap.append(0x646, 41);
		m_CharCompareMap.append(0x647, 43);
		m_CharCompareMap.append(0x648, 42);
		m_CharCompareMap.append(0x649, 44);
		m_CharCompareMap.append(0x64A, 44);
		m_CharCompareMap.append(0x660, 0);
		m_CharCompareMap.append(0x661, 1);
		m_CharCompareMap.append(0x662, 2);
		m_CharCompareMap.append(0x663, 3);
		m_CharCompareMap.append(0x664, 4);
		m_CharCompareMap.append(0x665, 5);
		m_CharCompareMap.append(0x666, 6);
		m_CharCompareMap.append(0x667, 7);
		m_CharCompareMap.append(0x668, 8);
		m_CharCompareMap.append(0x669, 9);
		m_CharCompareMap.append(0x67E, 15);
		m_CharCompareMap.append(0x686, 19);
		m_CharCompareMap.append(0x698, 26);
		m_CharCompareMap.append(0x6AF, 38);
		m_CharCompareMap.append(0x6C1, 43);
		m_CharCompareMap.append(0x6C3, 43);
		m_CharCompareMap.append(0x6CC, 44);
	}

	public int CompareString(String sLeft, String sRight)
	{
		sLeft = sLeft.toLowerCase(Locale.US);
		sRight = sRight.toLowerCase(Locale.US);

		int iLeftSize = sLeft.length();
		int iRightSize = sRight.length();

		CharSequence LeftStringChars = sLeft;
		CharSequence RightStringChars = sRight;

		for (int iIndex = 0; iIndex < iLeftSize; iIndex++)
		{
			if (iIndex < iRightSize)
			{
				int iLeftChar = LeftStringChars.charAt(iIndex);
				int iRightChar = RightStringChars.charAt(iIndex);

				int iLeftPersianOrder = -1;
				int iRightPersianOrder = -1;

				boolean bLeftIsNumber = iLeftChar >= NUMBERS_FIRST_CHAR && iLeftChar <= NUMBERS_LAST_CHAR;
				boolean bRightIsNumber = iRightChar >= NUMBERS_FIRST_CHAR && iRightChar <= NUMBERS_LAST_CHAR;
				iLeftPersianOrder = m_CharCompareMap.get(iLeftChar, -1);
				iRightPersianOrder = m_CharCompareMap.get(iRightChar, -1);
				boolean bLeftIsPersian = iLeftPersianOrder != -1;
				boolean bRightIsPersian = iRightPersianOrder != -1;
				boolean bLeftIsEnglish = iLeftChar >= ENGLISH_SMALL_FIRST_CHAR && iLeftChar <= ENGLISH_SMALL_END_CHAR;
				boolean bRightIsEnglish = iRightChar >= ENGLISH_SMALL_FIRST_CHAR && iRightChar <= ENGLISH_SMALL_END_CHAR;

				if (bLeftIsPersian && bRightIsPersian)
				{
					if (iLeftPersianOrder < iRightPersianOrder)
						return -1;
					else if (iLeftPersianOrder > iRightPersianOrder)
						return 1;
				}
				else if (bLeftIsNumber && bRightIsPersian)
					return -1;
				else if (bRightIsPersian && bRightIsNumber)
					return 1;
				else if (bLeftIsNumber && bRightIsEnglish)
					return -1;
				else if (bLeftIsEnglish && bRightIsNumber)
					return 1;
				else if (bLeftIsPersian && bRightIsEnglish)
					return -1;
				else if (bLeftIsEnglish && bRightIsPersian)
					return 1;
				else
				{
					if (iLeftChar < iRightChar)
						return -1;
					else if (iLeftChar > iRightChar)
						return 1;
				}
			}
		}

		if (iLeftSize < iRightSize)
			return -1;
		else if (iLeftSize > iRightSize)
			return 1;
		else
			return 0;
	}
}
